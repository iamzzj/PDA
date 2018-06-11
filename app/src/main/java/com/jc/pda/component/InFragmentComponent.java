package com.jc.pda.component;

import com.jc.pda.module.ContextModule;
import com.jc.pda.module.InFragmentModule;
import com.jc.pda.tableFragment.InFragment;

import dagger.Component;

/**
 * Created by z on 2018/1/3.
 */
@Component(modules = {ContextModule.class, InFragmentModule.class})
public interface InFragmentComponent {
    void inject(InFragment inFragment);
}
