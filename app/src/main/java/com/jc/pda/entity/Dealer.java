package com.jc.pda.entity;

/**
 * Created by z on 2017/12/28.
 */

public class Dealer {
    private String dealerNo;
    private String dealerName;

    public Dealer() {
    }

    public Dealer(String dealerNo, String dealerName) {
        this.dealerNo = dealerNo;
        this.dealerName = dealerName;
    }

    public String getDealerNo() {
        return dealerNo;
    }

    public void setDealerNo(String dealerNo) {
        this.dealerNo = dealerNo;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @Override
    public String toString() {
        return "Dealer{" +
                "dealerNo='" + dealerNo + '\'' +
                ", dealerName='" + dealerName + '\'' +
                '}';
    }
}
