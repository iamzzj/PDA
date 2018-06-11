package com.jc.pda.module;

import com.jc.pda.presenter.view.BatchActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by z on 2017/12/28.
 */
@Module
public class BatchActivityModule {
    private BatchActivityView batchActivityView;

    public BatchActivityModule(BatchActivityView batchActivityView) {
        this.batchActivityView = batchActivityView;
    }

    @Provides
    public BatchActivityView provideBatchActivityView(){
        return this.batchActivityView;
    }
}
