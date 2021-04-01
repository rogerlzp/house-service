package com.house.backend.houseservice.exception;

import com.alibaba.fastjson.JSON;
import com.house.backend.houseservice.common.AjaxResult;
import com.house.backend.houseservice.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengshuqin
 */
@Order(-1000)
@Component
@Slf4j
public class HouseExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        AjaxResult result = new AjaxResult();

        result.setSuccess(false);
        //处理异常
        if (ex instanceof HouseException) {
            resolverBussinessException(ex, result);
        } else if (ex instanceof BindException) {
            resolverBindException(ex, result);
        } else {
            resolverOtherException(ex, result);
        }
        if (ex instanceof FileException) {
            response.setStatus(500);
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");

        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            log.error("与客户端通讯异常：" + e.getMessage(), e);
        }


        log.error(ex.getMessage(),ex);
        return new ModelAndView();
    }

    /**
     * 处理业务层异常
     */
    private void resolverBussinessException(Exception ex, AjaxResult result) {
        HouseException businessException = (HouseException) ex;
        addResult(result, "业务异常：" + businessException.getErrorMessage());
        result.setCode(businessException.getErrorCode());
        //返回登录页面
        if (Status.CODE_AUTHORIZATION_GET_USERINFO.getCode().equalsIgnoreCase(businessException.getErrorCode())
                || Status.CODE_AUTHORIZATION_TOKEN_IS_NULL.getCode().equalsIgnoreCase(businessException.getErrorCode())
                || Status.CODE_BUSINESS_LOGIN_INVALID.getCode().equalsIgnoreCase(businessException.getErrorCode())
                || Status.CODE_AUTHORIZATION_SYSKEY_IS_NULL.getCode().equalsIgnoreCase(businessException.getErrorCode())) {
            Map map = new HashMap<>();
            map.put("redirect", "/#login");
            result.setCode("AU9999");
            result.setSuccess(false);
            result.setData(map);
        }
    }

    /**
     * 处理参数绑定异常
     */
    private void resolverBindException(Exception ex, AjaxResult result) {

        BindException be = (BindException) ex;
        List<FieldError> errorList = be.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError error : errorList) {
            sb.append(error.getObjectName());
            sb.append("对象的");
            sb.append(error.getField());
            sb.append("字段");
            sb.append(error.getDefaultMessage());
        }
        result.setCode("9999");
        addResult(result, "参数传递异常:" + sb.toString());
    }

    /**
     * 处理其他异常
     */
    private void resolverOtherException(Exception ex, AjaxResult result) {
        result.setCode("9999");
        addResult(result, ex.getMessage());
    }

    /**
     * 封装code和msg
     */
    private void addResult(AjaxResult result, String msg) {
        result.setMessage(msg);
    }


}
