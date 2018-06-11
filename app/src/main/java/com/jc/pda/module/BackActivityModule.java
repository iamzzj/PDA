package com.jc.pda.module;

import com.jc.pda.presenter.view.BackActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2018/1/4.
 */
@Module
public class BackActivityModule {
    private BackActivityView backActivityView;

    public BackActivityModule(BackActivityView backActivityView) {
        this.backActivityView = backActivityView;
    }

    @Provides
    public BackActivityView provideBackActivityView(){
        return this.backActivityView;
    }
}
