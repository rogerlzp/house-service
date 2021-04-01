package com.house.backend.houseservice.common;

import com.house.backend.houseservice.enums.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengshuqin
 */
@ToString
@Setter
@Getter
public class AjaxResult<T> {
    private String code = "0000";
    private boolean success = true;
    private String message;
    private T data;

    public AjaxResult(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
        if (!this.code.equalsIgnoreCase(code)) {
            this.success = false;
        }
    }

    public AjaxResult(boolean code, String message) {
        this.code = code ? "0000" : "9999";
        this.success = code;
        this.message = message;
        this.data = null;
    }

    public AjaxResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        if (!this.code.equalsIgnoreCase(code)) {
            this.success = false;
        }
    }

    public AjaxResult(boolean code, T data) {
        this.code = code ? "0000" : "9999";
        this.success = code;
        this.message = null;
        this.data = data;
    }

    public AjaxResult() {
        this.code = "0000";
        this.message = "success";
        this.success = true;
        this.data = null;
    }

    public static <T> AjaxResult<T> ok(T data) {
        AjaxResult result = new AjaxResult();
        result.setCode("0000");
        result.setMessage("success");
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static <T> AjaxResult<T> ok(String code, String message, T data) {
        AjaxResult result = new AjaxResult();
        result.setCode("0000");
        result.setMessage(message);
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static AjaxResult ok() {
        AjaxResult result = new AjaxResult();
        result.setCode("0000");
        result.setMessage("操作成功");
        result.setSuccess(true);
        result.setData(null);
        return result;
    }


    public static AjaxResult ok(String key, Object val) {
        AjaxResult result = new AjaxResult();
        result.setCode("0000");
        result.setMessage("操作成功");
        result.setSuccess(true);

        Map map = new HashMap();
        map.put(key, val);

        result.setData(map);
        return result;
    }

    public static AjaxResult error() {
        AjaxResult result = new AjaxResult();
        result.setCode("9999");
        result.setMessage("操作失败!");
        result.setSuccess(false);
        result.setData(null);
        return result;
    }

    public static AjaxResult error(String code, String message) {
        AjaxResult result = new AjaxResult();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);

        if (!"0000".equalsIgnoreCase(code)) {
            result.setSuccess(false);
        }
        return result;
    }

    public static AjaxResult error(Status exception) {
        AjaxResult result = new AjaxResult();
        result.setCode(exception.getCode());
        result.setMessage(exception.getMsg());
        result.setData(null);

        if (!"0000".equalsIgnoreCase(exception.getCode())) {
            result.setSuccess(false);
        }
        return result;
    }
}
