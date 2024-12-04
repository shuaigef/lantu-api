package com.shuaigef.lantuapibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shuaigef.lantuapicommon.model.entity.UserInterfaceInfo;
import java.util.List;

/**
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 **/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}




