package com.house.backend.houseservice.enums;

import lombok.Getter;

/**
 * @author zhengshuqin
 * 条件查询枚举
 */

@Getter
public enum ConditonTypeEnum {
    /*条件查询枚举*/
    LIKE("like", "Like", 1),
    NOTLIKE("notLike", "notLike", 1),
    Equal("=", "EqualTo", 1),
    NotEqual("<>", "NotEqualTo", 1),
    Or("or", "or", 0),
    AND("and", "and", 0),
    ISNOTNULL("isNotNull", "IsNotNull", 0),
    GreaterThanOrEqualTo(">=", "GreaterThanOrEqualTo", 1),
    andIdLessThanOrEqualTo("<=", "LessThanOrEqualTo", 1),
    In("in", "In", 1);

    private String value;
    private String condtion;
    private Integer paramNum;

    ConditonTypeEnum(String value, String condtion, Integer paramNum) {
        this.value = value;
        this.condtion = condtion;
        this.paramNum = paramNum;
    }

    public static String getEnumDescIgnoreCase(String value) {

        ConditonTypeEnum[] values = values();
        for (ConditonTypeEnum obj : values) {
            if (obj.value.equalsIgnoreCase(value)) {
                return obj.condtion;
            }
        }
        return "";
    }

    public static Integer getParamNumIgnoreCase(String value) {

        ConditonTypeEnum[] values = values();
        for (ConditonTypeEnum obj : values) {
            if (obj.value.equalsIgnoreCase(value)) {
                return obj.paramNum;
            }
        }
        return 0;
    }
}
