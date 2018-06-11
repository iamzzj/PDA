package com.jc.pda.presenter;

import android.content.Context;

import com.jc.pda.database.helper.BatchHelper;
import com.jc.pda.entity.Batch;
import com.jc.pda.presenter.view.BatchActivityView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by z on 2017/12/28.
 */

public class BatchActivityPresenter {
    private Context context;
    private BatchActivityView batchActivityView;

    @Inject
    public BatchActivityPresenter(Context context, BatchActivityView batchActivityView) {
        this.context = context;
        this.batchActivityView = batchActivityView;
    }

    public void getBatchs(){
        Observable.create(new ObservableOnSubscribe<List<Batch>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Batch>> e) throws Exception {
                List<Batch> batchs = BatchHelper.getSingleton(context).getBatchs(batchActivityView.getProductId());
                e.onNext(batchs);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Batch>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Batch> batches) {
                        batchActivityView.setBatchs(batches);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        batchActivityView.dialogDismiss();
                    }
                });
    }
}
