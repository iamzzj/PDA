package com.jc.pda.module;

import com.jc.pda.presenter.view.MyFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2017/12/22.
 */
@Module
public class MyFragmentModule {
    private MyFragmentView myFragmentView;

    public MyFragmentModule(MyFragmentView myFragmentView){
        this.myFragmentView = myFragmentView;
    }

    @Provides
    public MyFragmentView provideMyFragmentView(){
        return this.myFragmentView;
    }

}
