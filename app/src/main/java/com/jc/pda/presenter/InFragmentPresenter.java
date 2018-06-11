package com.jc.pda.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.jc.pda.database.helper.BatchHelper;
import com.jc.pda.database.helper.BillHelper;
import com.jc.pda.database.helper.ProductHelper;
import com.jc.pda.entity.Batch;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.BillCharts;
import com.jc.pda.entity.Product;
import com.jc.pda.entity.ProductAndBatch;
import com.jc.pda.entityEventBus.DownLoadSuccess;
import com.jc.pda.presenter.view.InFragmentView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.TextUtil;
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
 * Created by z on 2018/1/3.
 */

public class InFragmentPresenter {
    private Context context;
    private InFragmentView inFragmentView;

    @Inject
    public InFragmentPresenter(Context context, InFragmentView inFragmentView) {
        this.context = context;
        this.inFragmentView = inFragmentView;

        EventBus.getDefault().register(this);
    }

    public void getAllBills(){
        Observable.create(new ObservableOnSubscribe<List<Bill>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Bill>> e) throws Exception {
                List<Bill> bills = BillHelper.getSingleton(context).getBills(Constant.IN,inFragmentView.getTime(),inFragmentView.getProductId(),inFragmentView.getBatchId());
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
                        inFragmentView.setBills(bills);
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
                    dates.add(TimeUtils.getYesterday(inFragmentView.getTime(),i));
                }
                Logger.i(dates.toString());

                List<Integer> noUpBillCounts = new ArrayList<Integer>();
                List<Integer> upBillCounts= new ArrayList<Integer>();
                List<Integer> reUpBillCounts= new ArrayList<Integer>();

                for (String date : dates){
                    noUpBillCounts.add(BillHelper.getSingleton(context).getBillCount(date,Constant.IN,Constant.FISTUP,false));
                    upBillCounts.add(BillHelper.getSingleton(context).getBillCount(date,Constant.IN,Constant.FISTUP,true));
                    reUpBillCounts.add(BillHelper.getSingleton(context).getBillCount(date,Constant.IN,Constant.REUP,true));
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
                        inFragmentView.setBillCharts(billCharts);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getProductAndBatch(){
        Observable.create(new ObservableOnSubscribe<ProductAndBatch>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ProductAndBatch> e) throws Exception {
                List<Product> products = new ArrayList<Product>();
                List<List<Batch>> batchs = new ArrayList<List<Batch>>();
                List<Product> productsCache = ProductHelper.getSingleton(context).getProducts();
                //添加第一个全部
                products.add(new Product("","全部"));
                for (Product p:productsCache){
                    products.add(p);
                }

                List<String> productStrings = new ArrayList<String>();
                List<List<String>> batchStrings = new ArrayList<List<String>>();

                if(products!=null&&products.size()>0) {
                    for (Product product : products) {
                        List<Batch> bs = new ArrayList<Batch>();
                        if("全部".equals(product.getProductName())) {
                            bs.add(new Batch("","全部"));
                        }else{
                            bs = BatchHelper.getSingleton(context).getBatchs(product.getProductNo());
                        }
                        batchs.add(bs);

                        productStrings.add(product.getProductName());

                        List<String> b = new ArrayList<String>();
                        if(bs!=null && bs.size()>0){
                            for (Batch batch : bs){
                                b.add(batch.getBatchName());
                            }
                        }
                        batchStrings.add(b);
                    }
                }

                e.onNext(new ProductAndBatch(productStrings,batchStrings,products,batchs));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProductAndBatch>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProductAndBatch productAndBatch) {
                        inFragmentView.setProdutAndBatch(productAndBatch);
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
        getProductAndBatch();
    }

    public void unregister(){
        EventBus.getDefault().unregister(this);
    }
}
