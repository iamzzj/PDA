package com.jc.pda.component;

import com.jc.pda.activity.OutActivity;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.OutActivityModule;

import dagger.Component;

/**
 * Created by z on 2018/1/3.
 */
@Component(modules = {ContextModule.class, OutActivityModule.class})
public interface OutActivityComponent {
    void inject(OutActivity outActivity);
}
