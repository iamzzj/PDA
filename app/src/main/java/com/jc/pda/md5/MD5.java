package com.jc.pda.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by z on 2017/11/17.
 */

public class MD5 {
    private static BASE64Encoder encoder = new BASE64Encoder();
    private static BASE64Decoder decoder = new BASE64Decoder();

    public static String base64encode(String s) {
        try {
            String encodeStr = encoder.encode(s.getBytes());
            return encodeStr;
        } catch (Exception e) {
            return s;
        }
    }

    public static String base64decode(String s) {
        try {
            String decodeStr = new String(decoder.decodeBuffer(s));
            return decodeStr;
        } catch (Exception e) {
            return s;
        }
    }

    public static byte[] md5encode(byte[] input) {
        byte[] digestedValue = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input);
            digestedValue = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digestedValue;
    }

    public static String getMd5Value(String sSecret) {

        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
