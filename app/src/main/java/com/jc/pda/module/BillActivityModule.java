package com.jc.pda.module;

import com.jc.pda.presenter.view.BillActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2018/1/5.
 */
@Module
public class BillActivityModule {
    private BillActivityView billActivityView;

    public BillActivityModule(BillActivityView billActivityView) {
        this.billActivityView = billActivityView;
    }

    @Provides
    public BillActivityView provideBillActivityView(){
        return this.billActivityView;
    }
}
