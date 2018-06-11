package com.jc.pda.component;

import com.jc.pda.activity.LoginActivity;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.LoginActivityModule;

import dagger.Component;

/**
 * Created by z on 2017/12/22.
 */
@Component(modules = {LoginActivityModule.class, ContextModule.class})
public interface LoginActivityComponent {
    void inject(LoginActivity loginActivity);
}
