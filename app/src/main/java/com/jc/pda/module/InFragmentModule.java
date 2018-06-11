package com.jc.pda.module;

import com.jc.pda.presenter.view.InFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2018/1/3.
 */
@Module
public class InFragmentModule {
    private InFragmentView inFragmentView;

    public InFragmentModule(InFragmentView inFragmentView) {
        this.inFragmentView = inFragmentView;
    }

    @Provides
    public InFragmentView provideInFragmentView(){
        return this.inFragmentView;
    }
}
