package com.xxs.leon.xxs.rest.bean.response;

import com.xxs.leon.xxs.rest.bean.ErrorBean;

/**
 * Created by maliang on 15/12/21.
 */
public class PayEntity extends ErrorBean{
    private String html;
    private String out_trade_no;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }
}
