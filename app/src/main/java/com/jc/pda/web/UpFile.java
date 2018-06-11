package com.jc.pda.web;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;
import com.jc.pda.entity.UpDate;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.PermissionUtils;
import com.jc.pda.utils.SharedPreUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by z on 2018/1/8.
 */

public class UpFile {
    public static boolean upFile(String actionUrl, Map<String, String> params,
                              String filename, String fileDataString) {
        try {
            String BOUNDARY = java.util.UUID.randomUUID().toString();
            // String BOUNDARY="---------------------------7db1471890440";
            String PREFIX = "--", LINEND = "\r\n";
            String MULTIPART_FROM_DATA = "multipart/form-data";
            String CHARSET = "UTF-8";
            URL uri = new URL(actionUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout(1080000 * 1000);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false);
            conn.setRequestMethod("POST"); // Post方式
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "GBK");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                    + ";boundary=" + BOUNDARY);

            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
            if (params != null) {
                // 首先组拼文本类型的参数
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    sb.append("Content-Disposition: form-data; name=\""
                            + "file" + "\"" + LINEND);
                    sb.append("Content-Type: text/plain; charset=" + CHARSET
                            + LINEND);
                    sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                    sb.append(LINEND);
                    sb.append(entry.getValue());
                    sb.append(LINEND);
                }
                outStream.write(sb.toString().getBytes());
            }

            // 发送文件数据
            StringBuilder sb1 = new StringBuilder();
            sb1.append(PREFIX);
            sb1.append(BOUNDARY);
            sb1.append(LINEND);
            sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + filename + "\"" + LINEND);
            sb1.append("Content-Type: application/octet-stream; charset="
                    + CHARSET + LINEND);
            sb1.append(LINEND);
            outStream.write(sb1.toString().getBytes());
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    fileDataString.getBytes());
            // InputStream is = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bais.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }

            bais.close();
            outStream.write(LINEND.getBytes());

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();

            String result = responseData(conn.getInputStream());

            if("OK".equals(result.replace('\n', ' ').trim())){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private static String responseData(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        String line;
        StringBuilder sb = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(inputStream));
            sb = new StringBuilder();

            while ((line = in.readLine()) != null) {
                sb.append(line + '\n');
            }
            in.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String createUpFileName(Context context,String name,int type){
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("_");
        builder.append(new UpDate().getUpDateDetail());
        builder.append("_");
        /*androidID*/
        builder.append(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(PermissionUtils.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)){
                builder.append("_");
                /*IMEI*/
                builder.append(((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
            }
        }else{
            builder.append("_");
            /*IMEI*/
            builder.append(((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
        }
        switch (type){
            case Constant.IN:
                builder.append(".l.txt");
                break;
            case Constant.OUT:
                builder.append(".o.txt");
                break;
            case Constant.BACK:
                builder.append(".b.txt");
                break;
        }

        return builder.toString();
    }

    public static String createUpFileContent(Bill bill, List<Code> codes,Context context,int type){
        StringBuilder sBuilder = new StringBuilder();
        String split = ",";
        if(codes!=null && codes.size()>0){
            if(type == Constant.IN) {
                for (int i = 0; i < codes.size(); i++) {
                    sBuilder.append(bill.getProductId());
                    sBuilder.append(split);
                    sBuilder.append(codes.get(i).getCode());
                    sBuilder.append(split);
                    sBuilder.append("Y");
                    sBuilder.append(split);
                    sBuilder.append(codes.get(i).getUpDateString());
                    sBuilder.append(split);
                    sBuilder.append(SharedPreUtil.getLoginUser(context));
                    sBuilder.append(split);
                    sBuilder.append(split);
                    sBuilder.append(bill.getBatchId());

                    if (i < codes.size() - 1) {
                        sBuilder.append("\r\n");
                    }
                }
            }else if(type == Constant.OUT){
                for (int i = 0;i<codes.size();i++){
                    sBuilder.append(bill.getDealerId());
                    sBuilder.append(split);
                    sBuilder.append(codes.get(i).getCode());
                    sBuilder.append(split);
                    sBuilder.append("Y");
                    sBuilder.append(split);
                    sBuilder.append(codes.get(i).getUpDateString());
                    sBuilder.append(split);
                    sBuilder.append(SharedPreUtil.getLoginUser(context));

                    if(i<codes.size()-1){
                        sBuilder.append("\r\n");
                    }
                }
            }else if(type == Constant.BACK){
                for (int i = 0;i<codes.size();i++){
                    sBuilder.append("t");
                    sBuilder.append(split);
                    sBuilder.append(codes.get(i).getCode());
                    sBuilder.append(split);
                    sBuilder.append("B");
                    sBuilder.append(split);
                    sBuilder.append(codes.get(i).getUpDateString());
                    sBuilder.append(split);
                    sBuilder.append(SharedPreUtil.getLoginUser(context));

                    if(i<codes.size()-1){
                        sBuilder.append("\r\n");
                    }
                }
            }
        }

        return sBuilder.toString();
    }

}
