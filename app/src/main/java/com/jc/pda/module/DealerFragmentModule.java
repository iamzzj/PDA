package com.jc.pda.module;

import com.jc.pda.presenter.view.DealerFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2017/12/28.
 */
@Module
public class DealerFragmentModule {
    private DealerFragmentView dealerFragmentView;

    public DealerFragmentModule(DealerFragmentView dealerFragmentView) {
        this.dealerFragmentView = dealerFragmentView;
    }

    @Provides
    public DealerFragmentView provideDealerFragmentView(){
        return this.dealerFragmentView;
    }
}
