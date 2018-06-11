package com.jc.pda.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jc.pda.entitynet.VER;
import com.jc.pda.presenter.view.MenuActivityView;
import com.jc.pda.web.RetrofitUtils;
import com.jc.pda.web.view.VerView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.FutureTask;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by z on 2018/1/16.
 */

public class MenuActivityPresenter {
    private Context context;
    private MenuActivityView menuActivityView;

    @Inject
    public MenuActivityPresenter(Context context, MenuActivityView menuActivityView) {
        this.context = context;
        this.menuActivityView = menuActivityView;
    }

    public void checkUpdate(){
        RetrofitUtils.getSingleton()
                .create(VerView.class)
                .ver()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VER>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull VER ver) {
                        Logger.i(ver.toString());
                        try {
                            if(ver!=null) {
                                if(!TextUtils.isEmpty(ver.getVersionCode())){
                                    int versionCode = context
                                            .getPackageManager()
                                            .getPackageInfo(
                                                    context.getPackageName(),
                                                    0).versionCode;
                                    String versionName = context.getPackageManager()
                                            .getPackageInfo(
                                                    context.getPackageName(),
                                                    0).versionName;
                                    int newVersionCode = Integer.valueOf(ver.getVersionCode());
                                    String newVersionName = ver.getVersionName();

                                    if(newVersionCode>versionCode){
                                        menuActivityView.alertUpdate(versionName,newVersionName,ver.getNote());
                                    }
                                }
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        /*Observable<String> o1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Logger.i("11");
                e.onNext("1");
            }
        });

        Observable<Integer> o2 = new Observable<Integer>() {
            @Override
            protected void subscribeActual(Observer<? super Integer> observer) {
                observer.onNext(1);
                Logger.i("21");
            }
        }.map(new Function<Integer, List<Integer>>() {
            @Override
            public List<Integer> apply(Integer integer) throws Exception {
                ArrayList<Integer> list =  new ArrayList<>();
                list.add(1);
                list.add(2);
                list.add(3);
                Logger.i("22");
                return list;
            }
        })
                .flatMap(new Function<List<Integer>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(List<Integer> integers) throws Exception {
                        Logger.i("23");
                        return Observable.fromIterable(integers);
                    }
                });

        Observable.zip(o1, o2, new BiFunction<String,Integer,String>() {

            @Override
            public String apply(String s, Integer integer) throws Exception {
                return s+"--"+integer;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Logger.i("----31--"+s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/

        String[] strs = {};
        Observable o1 = Observable.just(strs);
        Observable.zip(o1, new Function<Object[], String>() {
            @Override
            public String apply(Object[] objects) throws Exception {
                return null;
            }
        });

        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... strings) {
                return null;
            }
        }.execute();

        Glide.with(context).load("").into(new ImageView(context));


    }
}
