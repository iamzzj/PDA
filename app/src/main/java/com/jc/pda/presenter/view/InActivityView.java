package com.jc.pda.presenter.view;

import com.jc.pda.entity.ProductAndBatch;

/**
 * Created by z on 2017/12/28.
 */

public interface InActivityView extends InOutBackView{
    boolean getSBType();
    void setProdutAndBatch(ProductAndBatch produtAndBatch);
    void setgetGoodInfoByUPCSuccess();
}
