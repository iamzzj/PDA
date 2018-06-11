package com.jc.pda.module;

import com.jc.pda.presenter.view.OutFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2018/1/4.
 */
@Module
public class OutFragmentModule {
    private OutFragmentView outFragmentView;

    public OutFragmentModule(OutFragmentView outFragmentView) {
        this.outFragmentView = outFragmentView;
    }

    @Provides
    public OutFragmentView provideOutFragmentView(){
        return this.outFragmentView;
    }
}
