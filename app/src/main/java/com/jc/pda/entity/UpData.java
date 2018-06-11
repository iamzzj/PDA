package com.jc.pda.entity;

import java.util.List;

/**
 * Created by z on 2018/1/8.
 */

public class UpData {
    private boolean success;
    private Bill bill;
    private List<Code> codes;

    public UpData(boolean success, Bill bill, List<Code> codes) {
        this.success = success;
        this.bill = bill;
        this.codes = codes;
    }

    public UpData() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }

    @Override
    public String toString() {
        return "UpData{" +
                "success=" + success +
                ", bill=" + bill +
                ", codes=" + codes +
                '}';
    }
}
