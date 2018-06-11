package com.jc.pda.module;

import com.jc.pda.presenter.view.LoginActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2017/12/22.
 */
@Module
public class LoginActivityModule {
    private LoginActivityView loginActivityView;

    public LoginActivityModule(LoginActivityView loginActivityView) {
        this.loginActivityView = loginActivityView;
    }

    @Provides
    public LoginActivityView provideLoginActivityView(){
        return this.loginActivityView;
    }
}
