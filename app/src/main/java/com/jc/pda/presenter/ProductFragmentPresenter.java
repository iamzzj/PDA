package com.jc.pda.presenter;

import android.content.Context;
import android.util.Log;

import com.jc.pda.database.helper.ProductHelper;
import com.jc.pda.entity.Product;
import com.jc.pda.entityEventBus.DownLoadSuccess;
import com.jc.pda.presenter.view.ProductFragmentView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * Created by z on 2017/12/27.
 */
public class ProductFragmentPresenter {
    private ProductFragmentView productFragmentView;
    private Context conotext;

    @Inject
    public ProductFragmentPresenter(ProductFragmentView productFragmentView, Context conotext) {
        this.productFragmentView = productFragmentView;
        this.conotext = conotext;

        EventBus.getDefault().register(this);
    }

    public void getProducts(){
        Observable.create(new ObservableOnSubscribe<List<Product>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Product>> e) throws Exception {
                List<Product> products = ProductHelper.getSingleton(conotext).getProducts();
                e.onNext(products);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Product>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Product> products) {
                        productFragmentView.setProducts(products);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        productFragmentView.dialogDismiss();
                    }

                    @Override
                    public void onComplete() {
                        productFragmentView.dialogDismiss();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downLoadSuccess(DownLoadSuccess downLoadSuccess) {
        getProducts();
    }

    public void unregister(){
        EventBus.getDefault().unregister(this);
    }
}
