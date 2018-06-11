package com.jc.pda.component;

import com.jc.pda.menuFragment.MyFragment;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.MyFragmentModule;

import dagger.Component;

/**
 * Created by z on 2017/12/22.
 */
@Component(modules = {MyFragmentModule.class, ContextModule.class})
public interface MyFragmentComponent {
    void inject(MyFragment myFragment);
}
