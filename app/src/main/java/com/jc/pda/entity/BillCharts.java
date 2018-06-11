package com.jc.pda.entity;

import java.util.List;

/**
 * Created by z on 2018/1/3.
 */

public class BillCharts {
    private List<String> dates;
    private List<Integer> noUpBillCounts;
    private List<Integer> upBillCounts;
    private List<Integer> reUpBillCounts;

    public BillCharts(List<String> dates, List<Integer> noUpBillCounts, List<Integer> upBillCounts, List<Integer> reUpBillCounts) {
        this.dates = dates;
        this.noUpBillCounts = noUpBillCounts;
        this.upBillCounts = upBillCounts;
        this.reUpBillCounts = reUpBillCounts;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Integer> getNoUpBillCounts() {
        return noUpBillCounts;
    }

    public void setNoUpBillCounts(List<Integer> noUpBillCounts) {
        this.noUpBillCounts = noUpBillCounts;
    }

    public List<Integer> getUpBillCounts() {
        return upBillCounts;
    }

    public void setUpBillCounts(List<Integer> upBillCounts) {
        this.upBillCounts = upBillCounts;
    }

    public List<Integer> getReUpBillCounts() {
        return reUpBillCounts;
    }

    public void setReUpBillCounts(List<Integer> reUpBillCounts) {
        this.reUpBillCounts = reUpBillCounts;
    }

    @Override
    public String toString() {
        return "BillCharts{" +
                "dates=" + dates +
                ", noUpBillCounts=" + noUpBillCounts +
                ", upBillCounts=" + upBillCounts +
                ", reUpBillCounts=" + reUpBillCounts +
                '}';
    }
}
