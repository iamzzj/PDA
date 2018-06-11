package com.jc.pda.component;

import com.jc.pda.module.ContextModule;
import com.jc.pda.module.UpDataPopupWindowModule;
import com.jc.pda.view.UpDataPopupWindow;

import dagger.Component;

/**
 * Created by z on 2018/1/9.
 */
@Component(modules = {ContextModule.class, UpDataPopupWindowModule.class})
public interface UpDataPopupWindowComponent {
    void inject(UpDataPopupWindow upDataPopupWindow);
}
