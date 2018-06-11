package com.jc.pda.presenter.view;

import com.jc.pda.entity.Batch;

import java.util.List;

/**
 * Created by z on 2017/12/28.
 */

public interface BatchActivityView {
    String getProductId();
    void dialogDismiss();
    void setBatchs(List<Batch> batchs);
}
