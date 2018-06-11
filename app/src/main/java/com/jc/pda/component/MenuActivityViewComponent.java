package com.jc.pda.component;

import com.jc.pda.activity.MenuActivity;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.MenuActivityModule;

import dagger.Component;

/**
 * Created by z on 2018/1/16.
 */
@Component(modules = {ContextModule.class, MenuActivityModule.class})
public interface MenuActivityViewComponent {
    void inject(MenuActivity menuActivity);
}
