package com.jc.pda.component;

import com.jc.pda.activity.BatchActivity;
import com.jc.pda.module.BatchActivityModule;
import com.jc.pda.module.ContextModule;

import dagger.Component;

/**
 * Created by z on 2017/12/28.
 */
@Component(modules = {ContextModule.class, BatchActivityModule.class})
public interface BatchActivityComponent {
    void inject(BatchActivity batchActivity);
}
