package com.jc.pda.web;

import com.jc.pda.utils.Global;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by z on 2017/12/19.
 */

public class RetrofitUtils {
    private volatile static Retrofit retrofit;

    public static Retrofit getSingleton(){
        if (retrofit == null){
            synchronized (RetrofitUtils.class){
                if(retrofit == null){

                    retrofit = new Retrofit.Builder()
                            .baseUrl(Global.UPDATE_ROOT)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();

                }
            }
        }

        return retrofit;
    }

}
