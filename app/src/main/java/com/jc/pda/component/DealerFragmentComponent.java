package com.jc.pda.component;

import com.jc.pda.module.ContextModule;
import com.jc.pda.module.DealerFragmentModule;
import com.jc.pda.tableFragment.DealerFragment;

import dagger.Component;

/**
 * Created by z on 2017/12/28.
 */
@Component(modules = {ContextModule.class, DealerFragmentModule.class})
public interface DealerFragmentComponent {
    void inject(DealerFragment dealerFragment);
}
