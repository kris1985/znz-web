package com.znz.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Administrator on 2017/4/11.
 */
public class CommonResponse<T> implements Serializable {
    private static final long serialVersionUID = -3214885479484534221L;
    private T result;
    private String errorCode;
    private String errorMsg;
    private boolean success;

    public CommonResponse() {
    }

    public CommonResponse(T result) {
        this.result = result;
    }

    public CommonResponse(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
        success = true;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public String toString() {
        return new StringBuilder(Objects.toString(this)).append("result:").append(this.result).append("success:").append(success).append("errorCode:").append(this.errorCode).append("errorMsg:").append(this.errorMsg).toString();
    }
}
