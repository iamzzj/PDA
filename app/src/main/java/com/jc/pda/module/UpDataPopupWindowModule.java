package com.jc.pda.module;

import com.jc.pda.presenter.view.UpDataPopupWindowView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2018/1/9.
 */
@Module
public class UpDataPopupWindowModule {
    private UpDataPopupWindowView upDataPopupWindowView;

    public UpDataPopupWindowModule(UpDataPopupWindowView upDataPopupWindowView) {
        this.upDataPopupWindowView = upDataPopupWindowView;
    }

    @Provides
    public UpDataPopupWindowView provideUpDataPopupWindowView(){
        return this.upDataPopupWindowView;
    }
}
