package com.jc.pda.component;

import com.jc.pda.activity.InActivity;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.InActivityModule;

import dagger.Component;

/**
 * Created by z on 2017/12/28.
 */
@Component(modules = {ContextModule.class, InActivityModule.class})
public interface InActivityComponent {
    void inject(InActivity inActivity);
}
