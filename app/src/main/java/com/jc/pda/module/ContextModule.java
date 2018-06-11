package com.jc.pda.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2017/12/22.
 */
@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext(){
        return this.context;
    }
}
