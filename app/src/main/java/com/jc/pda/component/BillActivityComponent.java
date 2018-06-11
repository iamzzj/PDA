package com.jc.pda.component;

import com.jc.pda.activity.BillActivity;
import com.jc.pda.module.BillActivityModule;
import com.jc.pda.module.ContextModule;

import dagger.Component;

/**
 * Created by z on 2018/1/5.
 */
@Component(modules = {ContextModule.class, BillActivityModule.class})
public interface BillActivityComponent {
    void inject(BillActivity billActivity);
}
