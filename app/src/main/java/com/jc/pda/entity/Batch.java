package com.jc.pda.entity;

/**
 * Created by z on 2017/12/28.
 */

public class Batch {
    private String batchNo;
    private String batchName;

    public Batch() {
    }

    public Batch(String batchNo, String batchName) {
        this.batchNo = batchNo;
        this.batchName = batchName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batchNo='" + batchNo + '\'' +
                ", batchName='" + batchName + '\'' +
                '}';
    }
}
