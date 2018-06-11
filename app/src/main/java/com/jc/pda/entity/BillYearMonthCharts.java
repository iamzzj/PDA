package com.jc.pda.entity;

import java.util.List;

/**
 * Created by z on 2018/1/18.
 */

public class BillYearMonthCharts {
    private List<String> dates;
    private List<Integer> inBillCounts;
    private List<Integer> outBillCounts;
    private List<Integer> backBillCounts;

    public BillYearMonthCharts(List<String> dates, List<Integer> inBillCounts, List<Integer> outBillCounts, List<Integer> backBillCounts) {
        this.dates = dates;
        this.inBillCounts = inBillCounts;
        this.outBillCounts = outBillCounts;
        this.backBillCounts = backBillCounts;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Integer> getInBillCounts() {
        return inBillCounts;
    }

    public void setInBillCounts(List<Integer> inBillCounts) {
        this.inBillCounts = inBillCounts;
    }

    public List<Integer> getOutBillCounts() {
        return outBillCounts;
    }

    public void setOutBillCounts(List<Integer> outBillCounts) {
        this.outBillCounts = outBillCounts;
    }

    public List<Integer> getBackBillCounts() {
        return backBillCounts;
    }

    public void setBackBillCounts(List<Integer> backBillCounts) {
        this.backBillCounts = backBillCounts;
    }

    @Override
    public String toString() {
        return "BillYearMonthCharts{" +
                "dates=" + dates +
                ", inBillCounts=" + inBillCounts +
                ", outBillCounts=" + outBillCounts +
                ", backBillCounts=" + backBillCounts +
                '}';
    }
}
