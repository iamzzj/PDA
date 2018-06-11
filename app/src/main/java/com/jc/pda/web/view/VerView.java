package com.jc.pda.web.view;

import com.jc.pda.entitynet.VER;
import com.jc.pda.utils.Global;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by z on 2018/1/16.
 */

public interface VerView {
    @GET(Global.UPDATE_VER)
    Observable<VER> ver();
}
