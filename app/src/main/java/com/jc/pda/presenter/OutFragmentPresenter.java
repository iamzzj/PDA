package com.jc.pda.presenter;

import android.content.Context;

import com.jc.pda.database.helper.BillHelper;
import com.jc.pda.database.helper.DealerHelper;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.BillCharts;
import com.jc.pda.entity.Dealer;
import com.jc.pda.entityEventBus.DownLoadSuccess;
import com.jc.pda.presenter.view.OutFragmentView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.TimeUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * Created by z on 2018/1/4.
 */

public class OutFragmentPresenter {
    private Context context;
    private OutFragmentView outFragmentView;

    @Inject
    public OutFragmentPresenter(Context context, OutFragmentView outFragmentView) {
        this.context = context;
        this.outFragmentView = outFragmentView;

        EventBus.getDefault().register(this);
    }

    public void getAllBills(){
        Observable.create(new ObservableOnSubscribe<List<Bill>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Bill>> e) throws Exception {
                List<Bill> bills = BillHelper.getSingleton(context).getBills(Constant.OUT,outFragmentView.getTime(),outFragmentView.getDealerId());
                e.onNext(bills);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Bill>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Bill> bills) {
                        outFragmentView.setBills(bills);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getBillCount(){
        Observable.create(new ObservableOnSubscribe<BillCharts>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<BillCharts> e) throws Exception {
                List<String> dates = new ArrayList<String>();
                for(int i =6 ;i >=0;i--){
                    dates.add(TimeUtils.getYesterday(outFragmentView.getTime(),i));
                }
                Logger.i(dates.toString());

                List<Integer> noUpBillCounts = new ArrayList<Integer>();
                List<Integer> upBillCounts= new ArrayList<Integer>();
                List<Integer> reUpBillCounts= new ArrayList<Integer>();

                for (String date : dates){
                    noUpBillCounts.add(BillHelper.getSingleton(context).getBillCount(date,Constant.OUT,Constant.FISTUP,false));
                    upBillCounts.add(BillHelper.getSingleton(context).getBillCount(date,Constant.OUT,Constant.FISTUP,true));
                    reUpBillCounts.add(BillHelper.getSingleton(context).getBillCount(date,Constant.OUT,Constant.REUP,true));
                }

                BillCharts bc = new BillCharts(dates,noUpBillCounts,upBillCounts,reUpBillCounts);
                e.onNext(bc);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BillCharts>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BillCharts billCharts) {
                        outFragmentView.setBillCharts(billCharts);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getDealers(){
        Observable.create(new ObservableOnSubscribe<List<Dealer>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Dealer>> e) throws Exception {
                List<Dealer> dealers = new ArrayList<Dealer>();
                List<Dealer> dealersCache = DealerHelper.getSingleton(context).getDealers();
                dealers.add(new Dealer("","全部"));
                for(Dealer dealer : dealersCache){
                    dealers.add(dealer);
                }
                e.onNext(dealers);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Dealer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Dealer> dealers) {
                        List<String> ops = new ArrayList<String>();
                        if(dealers != null && dealers.size()>0){
                            for (Dealer dealer : dealers){
                                ops.add(dealer.getDealerName());
                            }
                        }
                        outFragmentView.setDealers(ops,dealers);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downLoadSuccess(DownLoadSuccess downLoadSuccess) {
        getDealers();
    }

    public void unregister(){
        EventBus.getDefault().unregister(this);
    }
}
