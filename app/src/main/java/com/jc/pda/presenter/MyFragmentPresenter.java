package com.jc.pda.presenter;

import android.content.Context;

import com.jc.pda.database.helper.BatchHelper;
import com.jc.pda.database.helper.DealerHelper;
import com.jc.pda.database.helper.ProductHelper;
import com.jc.pda.database.table.GoodTable;
import com.jc.pda.entityEventBus.DownLoadSuccess;
import com.jc.pda.entitynet.BatchNet;
import com.jc.pda.entitynet.DealerNet;
import com.jc.pda.entitynet.ProductNet;
import com.jc.pda.presenter.view.MyFragmentView;
import com.jc.pda.utils.JsonUtils;
import com.jc.pda.web.WebService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by z on 2017/12/22.
 */

public class MyFragmentPresenter {
    private MyFragmentView myFragmentView;
    private Context context;

    private boolean isDownLoad = false;

    @Inject
    public MyFragmentPresenter(MyFragmentView myFragmentView, Context context){
        this.myFragmentView = myFragmentView;
        this.context = context;
    }

    public void startDownLoad(){
        if(isDownLoad){
            myFragmentView.info("正在下载中");
            return;
        }
        myFragmentView.dialogShow();
        isDownLoad = true;
        getGuestInfo();
    }

    public void getProductInfo(){
        Observable.create(new ObservableOnSubscribe<List<ProductNet>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<ProductNet>> e) throws Exception {
                Object[] strs = {
                        "CodeValue",myFragmentView.getCodeValue(),
                        "PageIndex","1",
                        "PageSize", "10000"
                };
                String res = WebService.sendWebService("GetProductInfo",strs);
                JSONObject result = new JSONObject(res);
                if(result.getBoolean("Success")){
                    List<ProductNet> products = JsonUtils.string2ObjectArray(result.getString("Data"),ProductNet.class);
                    ProductHelper.getSingleton(context).deleteAll();
                    BatchHelper.getSingleton(context).deleteAll();
                    ProductHelper.getSingleton(context).insertProducts(products);
                    e.onNext(products);
                    e.onComplete();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<ProductNet>, ObservableSource<ProductNet>>() {
                    @Override
                    public ObservableSource<ProductNet> apply(@NonNull List<ProductNet> productNets) throws Exception {
                        return Observable.fromIterable(productNets);
                    }
                })
                .observeOn(Schedulers.computation())
                .map(new Function<ProductNet, List<BatchNet>>() {
                    @Override
                    public List<BatchNet> apply(@NonNull ProductNet productNet) throws Exception {
                        Object[] strs = {
                                "CodeValue",myFragmentView.getCodeValue(),
                                "PageIndex","1",
                                "PageSize", "10000",
                                "cGoodCode",productNet.getCGuestCode()
                        };
                        String s = WebService.sendWebService("GetBatchInfos",strs);

                        JSONObject result = new JSONObject(s);
                        List<BatchNet> batchNets = new ArrayList<BatchNet>();
                        if(result.getBoolean("Success")){
                            batchNets = JsonUtils.string2ObjectArray(result.getString("Data"),BatchNet.class);
                            BatchHelper.getSingleton(context).insertBatchs(batchNets,productNet);
                        }

                        return batchNets;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BatchNet>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<BatchNet> batchNets) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        myFragmentView.err("批号下载异常\n"+e.getMessage());
                        myFragmentView.dialogDismiss();
                        isDownLoad = false;

                        EventBus.getDefault().post(new DownLoadSuccess());
                    }

                    @Override
                    public void onComplete() {
                        myFragmentView.dialogDismiss();
                        myFragmentView.success("下载成功");
                        isDownLoad = false;

                        EventBus.getDefault().post(new DownLoadSuccess());
                    }
                });
    }

    public void getGuestInfo(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> e) throws Exception {
                Object[] strs = {
                        "CodeValue",myFragmentView.getCodeValue(),
                        "PageIndex","1",
                        "PageSize", "10000"
                };
                String result = WebService.sendWebService("GetGuestInfo",strs);
                e.onNext(result);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        try {
                            JSONObject result = new JSONObject(s);
                            if(result.getBoolean("Success")){
                                List<DealerNet> dealers = JsonUtils.string2ObjectArray(result.getString("Data"),DealerNet.class);
                                DealerHelper.getSingleton(context).deleteAll();
                                DealerHelper.getSingleton(context).insertDealers(dealers);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        myFragmentView.err("经销商下载异常\n"+e.getMessage());
                        myFragmentView.dialogDismiss();
                        isDownLoad = false;
                    }

                    @Override
                    public void onComplete() {
                        Logger.i("guest_end");
                        getProductInfo();
                    }
                });
    }

    /**
     * 获取数据库中的产品，批号，经销商条数
     */
    public void getCount(){
        int productCount = getProductCount();
        int batchCount = getBatchCount();
        int dealerCount = getDealerCount();

        Logger.i(productCount+"-"+batchCount+"-"+dealerCount);

        myFragmentView.setCount(productCount,batchCount,dealerCount);
    }

    public int getProductCount(){
        return ProductHelper.getSingleton(context).getCount();
    }

    public int getBatchCount(){
        return BatchHelper.getSingleton(context).getCount();
    }

    public int getDealerCount(){
        return DealerHelper.getSingleton(context).getCount();
    }

}
