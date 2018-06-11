package com.jc.pda.entity;

import java.util.List;

/**
 * Created by z on 2018/1/18.
 */

public class BillNearYearCharts {
    private List<String> times;
    private List<BillYearCharts> datas;

    public BillNearYearCharts() {
    }

    public BillNearYearCharts(List<String> times, List<BillYearCharts> datas) {
        this.times = times;
        this.datas = datas;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public List<BillYearCharts> getDatas() {
        return datas;
    }

    public void setDatas(List<BillYearCharts> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "BillNearYearCharts{" +
                "times=" + times +
                ", datas=" + datas +
                '}';
    }
}
