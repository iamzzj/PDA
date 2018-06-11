package com.jc.pda.entitynet;

import java.util.List;

/**
 * Created by z on 2018/1/3.
 */

public class GoodInfoByUPC {

    private String Method;
    private String Token;
    private boolean Success;
    private List<String> Errors;
    private String Data;
    private int Count;

    public void setMethod(String Method) {
        this.Method = Method;
    }

    public String getMethod() {
        return Method;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getToken() {
        return Token;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public boolean getSuccess() {
        return Success;
    }

    public void setErrors(List<String> Errors) {
        this.Errors = Errors;
    }

    public List<String> getErrors() {
        return Errors;
    }

    public void setData(String Data) {
        this.Data = Data;
    }

    public String getData() {
        return Data;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public int getCount() {
        return Count;
    }


    @Override
    public String toString() {
        return "GoodInfoByUPC{" +
                "Method='" + Method + '\'' +
                ", Token='" + Token + '\'' +
                ", Success=" + Success +
                ", Errors=" + Errors +
                ", Data='" + Data + '\'' +
                ", Count=" + Count +
                '}';
    }
}
