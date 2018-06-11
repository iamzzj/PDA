package com.jc.pda.module;

import com.jc.pda.presenter.view.StatisticalActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2018/1/18.
 */
@Module
public class StatisticalActivityModule {
    private StatisticalActivityView statisticalActivityView;

    public StatisticalActivityModule(StatisticalActivityView statisticalActivityView) {
        this.statisticalActivityView = statisticalActivityView;
    }

    @Provides
    public StatisticalActivityView provideStatisticalActivityView(){
        return this.statisticalActivityView;
    }
}
