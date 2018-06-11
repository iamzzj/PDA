package com.jc.pda.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.jc.pda.presenter.view.LoginActivityView;
import com.jc.pda.utils.SharedPreUtil;
import com.jc.pda.web.WebService;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by z on 2017/12/22.
 */

public class LoginActivityPresenter {
    private LoginActivityView loginActivityView;
    private Context context;

    @Inject
    public LoginActivityPresenter(LoginActivityView loginActivityView, Context context) {
        this.loginActivityView = loginActivityView;
        this.context = context;
    }

    public void checkLogin(){
        loginActivityView.dialogShow();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> e) throws Exception {
                JSONArray Browser = new JSONArray();
                Browser.put("01");
                Browser.put(" ");
                Object[] strs = {
                        "Browser",Browser,
                        "LoginName",loginActivityView.getUser(),
                        "PassWord",loginActivityView.getPwd(),
                        "SystemVerson", "null"
                };
                String result = WebService.sendWebService("CheckLogin",strs);
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
                                String Data = result.getString("Data");
                                String cID = new JSONObject(Data).getString("cID");
                                SharedPreUtil.saveUserCID(context,cID);

                                SharedPreUtil.saveLogin(context,loginActivityView.getUser(),loginActivityView.getPwd(),
                                        loginActivityView.isSave(),loginActivityView.isAuto());

                                loginActivityView.loginSuccess();

                            }else{
                                loginActivityView.err(result.getString("Errors"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loginActivityView.err("登录异常\n"+e.getMessage());
                        loginActivityView.dialogDismiss();
                    }

                    @Override
                    public void onComplete() {
                        loginActivityView.dialogDismiss();
                    }
                });
    }

    public void getLoginInfo(){
        loginActivityView.loginInfo(
                SharedPreUtil.getLoginUser(context),
                SharedPreUtil.getLoginPwd(context),
                SharedPreUtil.getIsSave(context),
                SharedPreUtil.getIsAuto(context)
        );
    }
}
