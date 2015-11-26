package com.xxs.leon.xxs.rest.bean;

/**
 * Created by maliang on 15/11/25.
 */
public class ErrorBean {
    private int code;
    private String error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 错误码和错误信息可以在这里匹配，屏蔽传递过来的英文信息
     * @return
     */
    public String getError() {
        return error == null ? "-" : error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
