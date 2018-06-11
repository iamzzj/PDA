package com.jc.pda.module;

import com.jc.pda.presenter.view.OutActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2018/1/3.
 */
@Module
public class OutActivityModule {
    private OutActivityView outActivityView;

    public OutActivityModule(OutActivityView outActivityView) {
        this.outActivityView = outActivityView;
    }

    @Provides
    public OutActivityView provideOutActivityView(){
        return this.outActivityView;
    }
}
