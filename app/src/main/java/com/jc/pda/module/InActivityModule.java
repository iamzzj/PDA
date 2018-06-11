package com.jc.pda.module;

import com.jc.pda.presenter.view.InActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2017/12/28.
 */
@Module
public class InActivityModule {
    private InActivityView inActivityView;

    public InActivityModule(InActivityView inActivityView) {
        this.inActivityView = inActivityView;
    }

    @Provides
    public InActivityView provideInActivityView(){
        return this.inActivityView;
    }
}
