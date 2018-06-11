package com.jc.pda.web;

import com.jc.pda.utils.Global;
import com.jc.pda.utils.JsonUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by z on 2017/11/17.
 */

public class WebService {

    public static String sendWebService(String methodname, Object strs[])
            throws HttpResponseException, IOException, XmlPullParserException {
        Logger.i(methodname+"---sub\n"+getData(strs));
        String result = "{}";
        // 创建HttpTransportSE传说对象 传入webservice服务器地址
        HttpTransportSE httpSE = new HttpTransportSE(Global.API, 10000);
        httpSE.debug = false;
        // 创建SoapObject对象，创建该对象时需要传入所要调用Wb Service的命名空间、Web Service方法名；
        SoapObject soapObject = new SoapObject(Global.NAME_SPACE, methodname);
        soapObject.addProperty("jsonStr", getData(strs));

        // 创建SoapSerializationEnelope对象，并传入SOAP协议的版本号；并设置对象的bodyOut属性
        SoapSerializationEnvelope soapserial = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        soapserial.bodyOut = soapObject;
        // 设置与.NET提供的Web service保持有良好的兼容性
        soapserial.dotNet = true;
        soapserial.encodingStyle = "UTF-8";
        // 添加HeaderProperty信息，解决调用call的时候报java.io.EOFException错误
        ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
        headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
        // 调用HttpTransportSE对象的call方法来调用 webserice
        httpSE.call(Global.NAME_SPACE+methodname, soapserial,
                headerPropertyArrayList);
        result = soapserial.getResponse().toString();

        Logger.i(methodname+"---result\n"+result);
        return result;
    }

    private static String getData(Object[] strs) {
        JSONObject jb = new JSONObject();
        try {
            if (strs.length % 2 == 0) {
                for (int i = 0; i < strs.length; i += 2) {
                    jb.put((String) strs[i], strs[i + 1]);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebClass wc = new WebClass();
        wc.setData(jb.toString());

        return JsonUtils.object2String(wc);
    }
}
