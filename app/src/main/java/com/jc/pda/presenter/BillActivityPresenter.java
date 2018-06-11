package com.jc.pda.presenter;

import android.content.Context;

import com.jc.pda.database.helper.BillHelper;
import com.jc.pda.database.helper.CodeHelper;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;
import com.jc.pda.presenter.view.BillActivityView;
import com.orhanobut.logger.Logger;

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
 * Created by z on 2018/1/5.
 */

public class BillActivityPresenter {
    private Context context;
    private BillActivityView billActivityView;

    @Inject
    public BillActivityPresenter(Context context, BillActivityView billActivityView) {
        this.context = context;
        this.billActivityView = billActivityView;
    }

    public void getCodes() {
        Observable.create(new ObservableOnSubscribe<List<Code>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Code>> e) throws Exception {
                List<Code> codes = CodeHelper.getSingleton(context).getCodesByBillId(billActivityView.getBillId());
                e.onNext(codes);
                Logger.i(codes.toString());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Code>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Code> codes) {
                        billActivityView.setCodes(codes);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getBill(){
        Observable.create(new ObservableOnSubscribe<Bill>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bill> e) throws Exception {
                Bill bill = BillHelper.getSingleton(context).getBillByBillId(billActivityView.getBillId());
                e.onNext(bill);
                Logger.i(bill.toString());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bill>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Bill bill) {
                        billActivityView.setBill(bill);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
