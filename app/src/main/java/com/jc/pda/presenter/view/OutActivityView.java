package com.jc.pda.presenter.view;

import com.jc.pda.entity.Dealer;

import java.util.List;

/**
 * Created by z on 2018/1/3.
 */

public interface OutActivityView extends InOutBackView{
    void setDealers(List<String> ops , List<Dealer> dealers);
    String dealersKeyword();
}
