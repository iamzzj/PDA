package com.jc.pda.presenter.view;

import com.jc.pda.entity.Dealer;

import java.util.List;

/**
 * Created by z on 2017/12/28.
 */

public interface DealerFragmentView {
    void setDealers(List<Dealer> dealers);
    void dialogDismiss();
}
