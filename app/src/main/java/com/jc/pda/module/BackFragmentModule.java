package com.jc.pda.module;

import com.jc.pda.presenter.view.BackFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2018/1/4.
 */
@Module
public class BackFragmentModule {
    private BackFragmentView backFragmentView;

    public BackFragmentModule(BackFragmentView backFragmentView) {
        this.backFragmentView = backFragmentView;
    }

    @Provides
    public BackFragmentView provideBackFragmentView(){
        return this.backFragmentView;
    }
}
