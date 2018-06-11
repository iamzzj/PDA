package com.jc.pda.module;

import com.jc.pda.presenter.view.MenuActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2018/1/16.
 */
@Module
public class MenuActivityModule {
    private MenuActivityView menuActivityView;

    public MenuActivityModule(MenuActivityView menuActivityView) {
        this.menuActivityView = menuActivityView;
    }

    @Provides
    public MenuActivityView provideMenuActivityView(){
        return this.menuActivityView;
    }
}
