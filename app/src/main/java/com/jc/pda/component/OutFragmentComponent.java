package com.jc.pda.component;

import com.jc.pda.module.ContextModule;
import com.jc.pda.module.OutFragmentModule;
import com.jc.pda.tableFragment.OutFragment;

import dagger.Component;

/**
 * Created by z on 2018/1/4.
 */
@Component(modules = {ContextModule.class, OutFragmentModule.class})
public interface OutFragmentComponent {
    void inject(OutFragment outFragment);
}
