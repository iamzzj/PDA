package com.jc.pda.presenter.view;

import com.jc.pda.entity.Dealer;

import java.util.List;

/**
 * Created by z on 2018/1/4.
 */

public interface BackActivityView extends InOutBackView{
    void setDealers(List<String> ops , List<Dealer> dealers);
    String getDealerId();
    void CheckSuccess(String code);
}
