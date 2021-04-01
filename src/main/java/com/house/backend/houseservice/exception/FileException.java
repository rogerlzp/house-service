package com.house.backend.houseservice.exception;


import com.house.backend.houseservice.enums.Status;

/**
 * @author zhengshuqin
 */
public class FileException extends RuntimeException {
    private String errorCode;
    private String errorMessage;

    private static final long serialVersionUID = 1L;


    public FileException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = "9999";
    }

    public FileException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public FileException(Status code) {
        super(code.getMsg());
        this.errorCode = code.getCode();
        this.errorMessage = code.getMsg();
    }

    public FileException(Status code, Throwable throwable) {
        super(code.getMsg(), throwable);
        this.errorCode = code.getCode();
        this.errorMessage = code.getMsg();
    }

    public FileException(String errorCode, String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "DataSimException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                "} " + super.toString();
    }
}
