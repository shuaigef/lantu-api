package com.shuaigef.lantuapigateway;

import com.shuaigef.lantuapicommon.common.ErrorCode;
import com.shuaigef.lantuapicommon.model.entity.InterfaceInfo;
import com.shuaigef.lantuapicommon.model.entity.User;
import com.shuaigef.lantuapicommon.service.InnerInterfaceInfoService;
import com.shuaigef.lantuapicommon.service.InnerUserInterfaceInfoService;
import com.shuaigef.lantuapicommon.service.InnerUserService;
import com.shuaigef.lantuapigateway.exception.GatewayException;
import com.shuaigef.lantuclientsdk.utils.SignUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 网关过滤器
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8081";
    private static final String GATEWAY_HOST = "http://localhost:8088";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.用户发送请求到 API 网关
        // 2.请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = GATEWAY_HOST + request.getPath().value();
        String uri = request.getURI().toString();
        String method = request.getMethod().toString();
        log.info("请求唯一标识: " + request.getId());
        log.info("请求路径: " + path);
        log.info("uri: " + uri);
        log.info("请求方法: " + method);
        log.info("请求参数: " + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址: " + sourceAddress);
        log.info("请求来源地址: " + request.getRemoteAddress());

        try {
            // 3.黑白名单
            if (!IP_WHITE_LIST.contains(sourceAddress)) {
                throw new GatewayException(ErrorCode.FORBIDDEN_ERROR, "您的 IP 已被列为黑名单");
            }
            // 4.用户鉴权
            HttpHeaders headers = request.getHeaders();
            String accessKey = headers.getFirst("accessKey");
            String nonce = headers.getFirst("nonce");
            String timeStamp = headers.getFirst("timeStamp");
            String sign = headers.getFirst("sign");
            String body = headers.getFirst("body");

            // 4.1 权限校验
            User invokeUser = innerUserService.getInvokeUser(accessKey);
            if (invokeUser == null) {
                throw new GatewayException(ErrorCode.FORBIDDEN_ERROR, "密钥错误");
            }
            if (Long.parseLong(nonce) > 10000) {
                throw new GatewayException(ErrorCode.FORBIDDEN_ERROR, "非法请求");
            }
            long currentTime = System.currentTimeMillis() / 1000;
            final long FIVE_MINUTES = 60 * 5l;
            if (currentTime - Long.parseLong(timeStamp) > FIVE_MINUTES) {
                throw new GatewayException(ErrorCode.FORBIDDEN_ERROR, "会话过期，请重试");
            }

            // 4.2 校验签名
            String serverSign = SignUtils.genSign(body, invokeUser.getSecretKey());
            if (!serverSign.equals(sign)) {
                throw new GatewayException(ErrorCode.FORBIDDEN_ERROR, "非法请求");
            }

            // 5.请求的模拟接口是否存在
            InterfaceInfo interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
            if (interfaceInfo == null) {
                throw new GatewayException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
            }

            // 是否有调用次数
            if ((invokeUser.getUserPoints() - interfaceInfo.getConsumePoints()) < 0) {
                throw new GatewayException(ErrorCode.OPERATION_ERROR, "余额不足");
            }
            // 6.请求转发，调用模拟接口, 响应日志
            return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId(), interfaceInfo.getConsumePoints());
        } catch (Exception e) {
            throw new GatewayException(ErrorCode.FORBIDDEN_ERROR, e.getMessage());
        }

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 666;
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId, int reducePoints) {
        // 获取原始的响应对象
        ServerHttpResponse originalResponse = exchange.getResponse();
        // 获取数据缓冲工厂
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        // 获取响应的状态码
        HttpStatus statusCode = originalResponse.getStatusCode();

        // 判断状态码是否为200 OK(按道理来说,现在没有调用,是拿不到响应码的,对这个保持怀疑 沉思.jpg)
        if(statusCode == HttpStatus.OK) {
            // 创建一个装饰后的响应对象(开始穿装备，增强能力)
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                // 重写writeWith方法，用于处理响应体的数据
                // 这段方法就是只要当我们的模拟接口调用完成之后,等它返回结果，
                // 就会调用writeWith方法,我们就能根据响应结果做一些自己的处理
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    log.info("body instanceof Flux: {}", (body instanceof Flux));
                    // 判断响应体是否是Flux类型
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        // 返回一个处理后的响应体
                        // (这里就理解为它在拼接字符串,它把缓冲区的数据取出来，一点一点拼接好)
                        return super.writeWith(fluxBody.map(dataBuffer -> {
                            // 8.调用成功，扣除积分
                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId, reducePoints);

                            // 读取响应体的内容并转换为字节数组
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);//释放掉内存
                            // 构建日志
                            StringBuilder sb2 = new StringBuilder(200);
                            sb2.append("<--- {} {} \n");
                            List<Object> rspArgs = new ArrayList<>();
                            rspArgs.add(originalResponse.getStatusCode());
                            //rspArgs.add(requestUrl);
                            String data = new String(content, StandardCharsets.UTF_8);//data
                            sb2.append(data);
                            log.info("响应结果: " + data);
                            // 将处理后的内容重新包装成DataBuffer并返回
                            return bufferFactory.wrap(content);
                        }));
                    } else {
                        log.error("<--- {} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            // 对于200 OK的请求,将装饰后的响应对象传递给下一个过滤器链,并继续处理(设置repsonse对象为装饰过的)
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }
        // 对于非200 OK的请求，直接返回，进行降级处理
        return chain.filter(exchange);
    }


}
