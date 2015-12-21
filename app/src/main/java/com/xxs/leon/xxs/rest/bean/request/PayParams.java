package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/12/21.
 */
public class PayParams {
    private String product_name;
    private String body;
    private double order_price;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }
}
