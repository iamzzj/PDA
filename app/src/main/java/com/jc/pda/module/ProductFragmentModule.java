package com.jc.pda.module;

import com.jc.pda.presenter.view.ProductFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2017/12/27.
 */
@Module
public class ProductFragmentModule {
    private ProductFragmentView productFragmentView;

    public ProductFragmentModule(ProductFragmentView productFragmentView) {
        this.productFragmentView = productFragmentView;
    }

    @Provides
    public ProductFragmentView provideProductFragmentView(){
        return this.productFragmentView;
    }
}
