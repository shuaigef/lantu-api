package com.shuaigef.lantuapicommon.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 接口状态枚举
 *
 * @author <a href="https://github.com/shuaigef">shuaigef</a>
 */
public enum UserInterfaceInfoStatusEnum {

    NORMAL("正常", 0),
    BAN("禁止", 1);

    private final String text;

    private final Integer value;

    UserInterfaceInfoStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static UserInterfaceInfoStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserInterfaceInfoStatusEnum anEnum : UserInterfaceInfoStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
