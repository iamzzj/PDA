package com.jc.pda.component;

import com.jc.pda.activity.StatisticalActivity;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.StatisticalActivityModule;

import dagger.Component;

/**
 * Created by z on 2018/1/18.
 */
@Component(modules = {ContextModule.class, StatisticalActivityModule.class})
public interface StatisticalActivityComponent {
    void inject(StatisticalActivity statisticalActivity);
}
