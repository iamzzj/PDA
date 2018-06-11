package com.jc.pda.component;

import com.jc.pda.activity.BackActivity;
import com.jc.pda.module.BackActivityModule;
import com.jc.pda.module.ContextModule;

import dagger.Component;

/**
 * Created by z on 2018/1/4.
 */
@Component(modules = {ContextModule.class, BackActivityModule.class})
public interface BackActivityComponent {
    void inject(BackActivity backActivity);
}
