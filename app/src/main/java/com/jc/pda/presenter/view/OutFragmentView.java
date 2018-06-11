package com.jc.pda.presenter.view;

import com.jc.pda.entity.Bill;
import com.jc.pda.entity.BillCharts;
import com.jc.pda.entity.Dealer;
import com.jc.pda.entity.ProductAndBatch;

import java.util.List;

/**
 * Created by z on 2018/1/4.
 */

public interface OutFragmentView {
    void setDealers(List<String> ops , List<Dealer> dealers);
    String getTime();
    String getDealerId();
    void setBills(List<Bill> bills);
    void setBillCharts(BillCharts billCharts);
}
