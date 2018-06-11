package com.jc.pda.component;

import com.jc.pda.module.BackFragmentModule;
import com.jc.pda.module.ContextModule;
import com.jc.pda.tableFragment.BackFragment;

import dagger.Component;

/**
 * Created by z on 2018/1/4.
 */
@Component(modules = {ContextModule.class, BackFragmentModule.class})
public interface BackFragmentComponent {
    void inject(BackFragment backFragment);
}
