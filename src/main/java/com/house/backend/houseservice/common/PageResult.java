package com.house.backend.houseservice.common;

import com.github.pagehelper.PageInfo;
import com.house.backend.houseservice.enums.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhengshuqin
 * 分页查询返回
 */
@ToString
@Setter
@Getter
public class PageResult<T> {
    private String code = "0000";
    private boolean success = true;
    private String message;
    private T data;
    private int currentPage;
    private int pageSize;
    private long total;

    public static <T> PageResult ok(T data, String message, int pageNum, int pageSize, int total) {
        PageResult result = new PageResult();
        result.setCode("0000");
        result.setMessage(message);
        result.setSuccess(true);
        result.setData(data);
        result.setCurrentPage(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(total);
        return result;
    }

    public static <T> PageResult<PageInfo<T>> ok(PageInfo<T> data) {
        PageResult result = new PageResult();
        result.setCode("0000");
        result.setMessage("success");
        result.setSuccess(true);
        result.setData(data.getList());
        result.setCurrentPage(data.getPageNum());
        result.setPageSize(data.getPageSize());
        result.setTotal(data.getTotal());
        return result;
    }

    public static PageResult error() {
        PageResult result = new PageResult();
        result.setCode("9999");
        result.setMessage("操作失败!");
        result.setSuccess(false);
        result.setData(null);
        return result;
    }

    public static PageResult error(String code, String message) {
        PageResult result = new PageResult();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);

        if (!"0000".equalsIgnoreCase(code)) {
            result.setSuccess(false);
        }
        return result;
    }

    public static PageResult error(Status exception) {
        PageResult result = new PageResult();
        result.setCode(exception.getCode());
        result.setMessage(exception.getMsg());
        result.setData(null);
        if (!"0000".equalsIgnoreCase(exception.getCode())) {
            result.setSuccess(false);
        }
        return result;
    }
}
