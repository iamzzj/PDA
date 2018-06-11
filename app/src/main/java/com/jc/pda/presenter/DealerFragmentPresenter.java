package com.jc.pda.presenter;

import android.content.Context;

import com.jc.pda.database.helper.DealerHelper;
import com.jc.pda.entity.Dealer;
import com.jc.pda.entityEventBus.DownLoadSuccess;
import com.jc.pda.presenter.view.DealerFragmentView;

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
 * Created by z on 2017/12/28.
 */

public class DealerFragmentPresenter {
    private Context context;
    private DealerFragmentView dealerFragmentView;

    @Inject
    public DealerFragmentPresenter(Context context, DealerFragmentView dealerFragmentView) {
        this.context = context;
        this.dealerFragmentView = dealerFragmentView;

        EventBus.getDefault().register(this);
    }

    public void getDealers(){
        Observable.create(new ObservableOnSubscribe<List<Dealer>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Dealer>> e) throws Exception {
                List<Dealer> dealers = DealerHelper.getSingleton(context).getDealers();
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
                        dealerFragmentView.setDealers(dealers);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dealerFragmentView.dialogDismiss();
                    }

                    @Override
                    public void onComplete() {
                        dealerFragmentView.dialogDismiss();
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
