package com.edu.feicui.news.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/12/9.
 */
public class Md5 {

    private static final char[] HEX_DIGITS = {'0','1','2','3','4','5','6','7','8',
            '9','A','B','C','D','E','F'
    };

    private static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String produceMd5(String link){
        byte[] data = messageDigest.digest(link.getBytes());
        return byteToHex(data);
    }

    private static String byteToHex(byte[] data){
        StringBuffer sb = new StringBuffer();
        for(byte b : data){
            String s = "" + HEX_DIGITS[(b & 0x0f)>>>4] + HEX_DIGITS[b & 0x0f];
            sb.append(s);
        }

        return sb.toString();
    }
}
