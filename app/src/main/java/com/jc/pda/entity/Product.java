package com.jc.pda.entity;

/**
 * Created by z on 2017/12/27.
 */

public class Product {
    private String productNo;
    private String productName;

    public Product() {
    }

    public Product(String productNo, String productName) {
        this.productNo = productNo;
        this.productName = productName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productNo='" + productNo + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}
