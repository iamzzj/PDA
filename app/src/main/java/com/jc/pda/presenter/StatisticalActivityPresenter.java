package com.jc.pda.presenter;

import android.content.Context;

import com.jc.pda.database.helper.BillHelper;
import com.jc.pda.entity.BillYearCharts;
import com.jc.pda.entity.BillYearMonthCharts;
import com.jc.pda.presenter.view.StatisticalActivityView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.TimeUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
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
 * Created by z on 2018/1/18.
 */

public class StatisticalActivityPresenter {
    private Context context;
    private StatisticalActivityView statisticalActivityView;

    @Inject
    public StatisticalActivityPresenter(Context context, StatisticalActivityView statisticalActivityView) {
        this.context = context;
        this.statisticalActivityView = statisticalActivityView;
    }

    public void getYearMonthBillCount(){
        Observable.create(new ObservableOnSubscribe<BillYearMonthCharts>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<BillYearMonthCharts> e) throws Exception {
                List<String> dates = new ArrayList<String>();
                for(int i =1 ;i <=12;i++){
                    dates.add(TimeUtils.getYearDate(statisticalActivityView.getTime(),i));
                }

                List<Integer> inBillCounts = new ArrayList<Integer>();
                List<Integer> outBillCounts= new ArrayList<Integer>();
                List<Integer> backBillCounts= new ArrayList<Integer>();

                for (String date : dates){
                    inBillCounts.add(BillHelper.getSingleton(context).getYearMonthBillCount(date, Constant.IN,statisticalActivityView.getUpType(),statisticalActivityView.getIsUp()));
                    outBillCounts.add(BillHelper.getSingleton(context).getYearMonthBillCount(date,Constant.OUT,statisticalActivityView.getUpType(),statisticalActivityView.getIsUp()));
                    backBillCounts.add(BillHelper.getSingleton(context).getYearMonthBillCount(date,Constant.BACK,statisticalActivityView.getUpType(),statisticalActivityView.getIsUp()));
                }

                BillYearMonthCharts bc = new BillYearMonthCharts(dates,inBillCounts,outBillCounts,backBillCounts);
                Logger.i(bc.toString());
                e.onNext(bc);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BillYearMonthCharts>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BillYearMonthCharts billYearCharts) {
                        statisticalActivityView.setYearMonthBillCount(billYearCharts);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        statisticalActivityView.refreshDismiss();
                    }

                    @Override
                    public void onComplete() {
                        statisticalActivityView.refreshDismiss();
                    }
                });
    }

    public void getYearBillPcCount(){
        Observable.create(new ObservableOnSubscribe<BillYearCharts>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<BillYearCharts> e) throws Exception {
                int in = BillHelper.getSingleton(context).getYearMonthBillCount(statisticalActivityView.getTime(), Constant.IN,statisticalActivityView.getUpType(),statisticalActivityView.getIsUp());
                int out = BillHelper.getSingleton(context).getYearMonthBillCount(statisticalActivityView.getTime(), Constant.OUT,statisticalActivityView.getUpType(),statisticalActivityView.getIsUp());
                int back = BillHelper.getSingleton(context).getYearMonthBillCount(statisticalActivityView.getTime(), Constant.BACK,statisticalActivityView.getUpType(),statisticalActivityView.getIsUp());

                e.onNext(new BillYearCharts(in,out,back));
                Logger.i(statisticalActivityView.getTime()+"--"+new BillYearCharts(in,out,back).toString());
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BillYearCharts>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BillYearCharts billYearCharts) {
                        statisticalActivityView.setYearBillPcCount(billYearCharts);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        statisticalActivityView.refreshDismiss();
                    }

                    @Override
                    public void onComplete() {
                        statisticalActivityView.refreshDismiss();
                    }
                });
    }
}
