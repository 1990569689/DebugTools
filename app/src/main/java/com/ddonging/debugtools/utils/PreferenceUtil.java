package com.ddonging.debugtools.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 记录用户名，密码之类的首选项
 */
public class PreferenceUtil {
    private static PreferenceUtil preference = null;
    private SharedPreferences sharedPreference;
    public static final String MAC = "MAC";
    public static final String isMac = "isMac";
    public static final String isRssi = "isRssi";
    public static final String isTime = "isTime";
    public static final String isAutoScroll = "isAutoScroll";

    public static final String isAutoReconnect = "isAutoReconnect";

    public static final String isCleanSession = "isCleanSession";

    public static final String timeout = "timeout";

    public static final String keepalive = "keepalive";


    public static final String isName = "isName";

    public static final String time = "time";

    public static final String operate = "operate";

    public static final String interval = "interval";
//    public static Boolean isMac = true;
//    public static Boolean isRssi = true;
//
//    public static int time=1000;
//    public static Boolean isMac = true;
    public static synchronized PreferenceUtil getInstance(String name,Context context) {
        if (preference == null)
            preference = new PreferenceUtil(name,context);
        return preference;
    }
    public PreferenceUtil(String name,Context context) {
        sharedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
    public String getMAC(String defValue) {
        String value = sharedPreference.getString(MAC, defValue);
        return value;
    }
    public void setMAC(String value) {
        Editor edit = sharedPreference.edit();
        edit.putString(MAC, value);
        edit.commit();
    }

    public Integer getTime(Integer defValue) {
        int value = sharedPreference.getInt(time, defValue);
        return value;
    }
    public void setTime(Integer value) {
        Editor edit = sharedPreference.edit();
        edit.putInt(time, value);
        edit.commit();
    }

    public Boolean getIsMac(Boolean defValue) {
        Boolean value = sharedPreference.getBoolean(isMac, defValue);
        return value;
    }
    public void setIsMac(Boolean value) {
        Editor edit = sharedPreference.edit();
        edit.putBoolean(isMac, value);
        edit.commit();
    }
    public Boolean getIsRssi(Boolean defValue) {
        Boolean value = sharedPreference.getBoolean(isRssi, defValue);
        return value;
    }
    public void setIsRssi(Boolean value) {
        Editor edit = sharedPreference.edit();
        edit.putBoolean(isRssi, value);
        edit.commit();
    }

    public Boolean getIsTime(Boolean defValue) {
        Boolean value = sharedPreference.getBoolean(isTime, defValue);
        return value;
    }
    public void setIsTime(Boolean value) {
        Editor edit = sharedPreference.edit();
        edit.putBoolean(isTime, value);
        edit.commit();
    }



    public Boolean getIsAutoScroll(Boolean defValue) {
        Boolean value = sharedPreference.getBoolean(isAutoScroll, defValue);
        return value;
    }
    public void setIsAutoScroll(Boolean value) {
        Editor edit = sharedPreference.edit();
        edit.putBoolean(isAutoScroll, value);
        edit.commit();
    }
    public Boolean getIsName(Boolean defValue) {
        Boolean value = sharedPreference.getBoolean(isName, defValue);
        return value;
    }
    public void setIsName(Boolean value) {
        Editor edit = sharedPreference.edit();
        edit.putBoolean(isName, value);
        edit.commit();
    }


    public Integer getInterval(Integer defValue) {
        int value = sharedPreference.getInt(interval, defValue);
        return value;
    }
    public void setInterval(Integer value) {
        Editor edit = sharedPreference.edit();
        edit.putInt(interval, value);
        edit.commit();
    }

    public Integer getOperate(Integer defValue) {
        int value = sharedPreference.getInt(operate, defValue);
        return value;
    }
    public void setOperate(Integer value) {
        Editor edit = sharedPreference.edit();
        edit.putInt(operate, value);
        edit.commit();
    }

    public Integer getTimeout(Integer defValue) {
        int value = sharedPreference.getInt(timeout, defValue);
        return value;
    }
    public void setTimeout(Integer value) {
        Editor edit = sharedPreference.edit();
        edit.putInt(timeout, value);
        edit.commit();
    }

    public Integer getKeepAlive(Integer defValue) {
        int value = sharedPreference.getInt(keepalive, defValue);
        return value;
    }
    public void setKeepalive(Integer value) {
        Editor edit = sharedPreference.edit();
        edit.putInt(keepalive, value);
        edit.commit();
    }
    public Boolean getIsAutoReconnect(Boolean defValue) {
        Boolean value = sharedPreference.getBoolean(isAutoReconnect, defValue);
        return value;
    }
    public void setIsAutoReconnect(Boolean value) {
        Editor edit = sharedPreference.edit();
        edit.putBoolean(isAutoReconnect, value);
        edit.commit();
    }


    public Boolean getIsCleanSession(Boolean defValue) {
        Boolean value = sharedPreference.getBoolean(isCleanSession, defValue);
        return value;
    }
    public void setIsCleanSession(Boolean value) {
        Editor edit = sharedPreference.edit();
        edit.putBoolean(isCleanSession, value);
        edit.commit();
    }





}
