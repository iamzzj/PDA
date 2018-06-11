package com.jc.pda.component;

import android.content.Context;

import com.jc.pda.module.ContextModule;
import com.jc.pda.module.ProductFragmentModule;
import com.jc.pda.tableFragment.ProductFragment;

import dagger.Component;

/**
 * Created by z on 2017/12/27.
 */
@Component(modules = {ContextModule.class,ProductFragmentModule.class})
public interface ProductFragmentComponent {
    void inject(ProductFragment productFragment);
}
