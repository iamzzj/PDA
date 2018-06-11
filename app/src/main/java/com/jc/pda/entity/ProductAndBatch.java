package com.jc.pda.entity;

import java.util.List;

/**
 * Created by z on 2017/12/28.
 */

public class ProductAndBatch {
    private List<String> productStrings;
    private List<List<String>> batchStrings;
    private List<Product> products;
    private List<List<Batch>> batchs;

    public ProductAndBatch(List<String> productStrings, List<List<String>> batchStrings, List<Product> products, List<List<Batch>> batchs) {
        this.productStrings = productStrings;
        this.batchStrings = batchStrings;
        this.products = products;
        this.batchs = batchs;
    }

    public List<String> getProductStrings() {
        return productStrings;
    }

    public void setProductStrings(List<String> productStrings) {
        this.productStrings = productStrings;
    }

    public List<List<String>> getBatchStrings() {
        return batchStrings;
    }

    public void setBatchStrings(List<List<String>> batchStrings) {
        this.batchStrings = batchStrings;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<List<Batch>> getBatchs() {
        return batchs;
    }

    public void setBatchs(List<List<Batch>> batchs) {
        this.batchs = batchs;
    }

    @Override
    public String toString() {
        return "ProductAndBatch{" +
                "productStrings=" + productStrings +
                ", batchStrings=" + batchStrings +
                ", products=" + products +
                ", batchs=" + batchs +
                '}';
    }
}
