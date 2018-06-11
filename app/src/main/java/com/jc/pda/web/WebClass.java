package com.jc.pda.web;

import android.util.Base64;

import com.jc.pda.md5.MD5;
import com.jc.pda.utils.Global;

/**
 * Created by z on 2017/11/17.
 */

public class WebClass {
    private String Token;
    private String Data;

    public String getToken() {
        return Token;
    }

    private void setToken(String token) {
        this.Token = token;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        this.Data = data;
        String bs64 = new String(Base64.encode(MD5.getMd5Value(data + Global.APPKEY).getBytes(),
                Base64.DEFAULT));
        bs64 = bs64.replace("\n", "");
        setToken(bs64);
    }

    @Override
    public String toString() {
        return "WebClass [Token=" + Token + ", Data=" + Data + "]";
    }
}
