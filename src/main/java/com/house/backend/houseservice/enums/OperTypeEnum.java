package com.house.backend.houseservice.enums;

import lombok.Getter;

/**
 * @author zhengshuqin
 * 数据传输状态
 */
@Getter
public enum OperTypeEnum {
    /*另存为、新增保存枚举*/
    TYPE_I("I", "新增"),
    TYPE_U("U", "更新"),
    TYPE_D("D", "删除");
    private String value;
    private String desc;

    OperTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getEnumDesc(String value) {

        OperTypeEnum[] values = values();
        for (OperTypeEnum obj : values) {
            if (obj.value.equals(value)) {
                return obj.desc;
            }
        }
        return "";
    }

    public static OperTypeEnum getEnumByValue(String value) {
        OperTypeEnum[] values = values();
        for (OperTypeEnum obj : values) {
            if (obj.value.equals(value)) {
                return obj;
            }
        }
        return null;
    }
}
