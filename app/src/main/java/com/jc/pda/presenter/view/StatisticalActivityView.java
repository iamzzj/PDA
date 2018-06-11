package com.jc.pda.presenter.view;

import com.jc.pda.entity.BillNearYearCharts;
import com.jc.pda.entity.BillYearCharts;
import com.jc.pda.entity.BillYearMonthCharts;

/**
 * Created by z on 2018/1/18.
 */

public interface StatisticalActivityView {
    String getTime();
    void setYearMonthBillCount(BillYearMonthCharts billYearCharts);
    void setYearBillPcCount(BillYearCharts billYearCharts);
    void setNearYearBillBcCount(BillNearYearCharts billNearYearCharts);
    int getUpType();
    boolean getIsUp();
    void refreshDismiss();
}
