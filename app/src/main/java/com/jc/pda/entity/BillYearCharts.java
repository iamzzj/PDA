package com.jc.pda.entity;

/**
 * Created by z on 2018/1/18.
 */

public class BillYearCharts {
    private int in;
    private int out;
    private int back;

    public BillYearCharts(int in, int out, int back) {
        this.in = in;
        this.out = out;
        this.back = back;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

    @Override
    public String toString() {
        return "BillYearCharts{" +
                "in=" + in +
                ", out=" + out +
                ", back=" + back +
                '}';
    }
}
