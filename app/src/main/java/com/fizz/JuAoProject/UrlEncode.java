package com.fizz.JuAoProject;

import android.util.Log;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by xiexinwang on 2017/7/18.
 * Url转码以及解码
 */

public class UrlEncode {
    private static final String TAG = "UrlEncode";

    static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            Log.d(TAG, "toURLEncoded: " + paramString);
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            Log.d(TAG, "toURLEncoded: " + paramString + localException);
        }

        return "";
    }

    public static String toURLDecoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }

        return "";
    }
}
