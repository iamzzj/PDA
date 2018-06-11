package com.jc.pda.presenter.view;

import com.jc.pda.entity.Bill;
import com.jc.pda.entity.BillCharts;
import com.jc.pda.entity.ProductAndBatch;

import java.util.List;

/**
 * Created by z on 2018/1/3.
 */

public interface InFragmentView {
    String getTime();
    String getProductId();
    String getBatchId();
    void setBills(List<Bill> bills);
    void setBillCharts(BillCharts billCharts);
    void setProdutAndBatch(ProductAndBatch produtAndBatch);
}
