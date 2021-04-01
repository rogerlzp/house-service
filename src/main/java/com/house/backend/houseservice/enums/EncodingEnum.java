package com.house.backend.houseservice.enums;

import lombok.Getter;

/**
 * @author Helen.Chen
 * 一天执行多次Or仅执行一次
 */
@Getter
public enum EncodingEnum {
    /*数据传输状态枚举*/
    UTF8("utf-8", "utf-8"),
    GBK("gbk", "gbk");
    private String value;
    private String desc;

    EncodingEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getEnumDesc(String value) {

        EncodingEnum[] values = values();
        for (EncodingEnum obj : values) {
            if (obj.value.equals(value)) {
                return obj.desc;
            }
        }
        return "";
    }
}
