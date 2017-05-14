package com.znz.vo;

import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Administrator on 2017/4/11.
 */
@ToString
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
