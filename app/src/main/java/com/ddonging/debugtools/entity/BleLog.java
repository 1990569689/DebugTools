package com.ddonging.debugtools.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class BleLog {
   static ArrayList<HashMap<String,Object>> log=new ArrayList<>();

    public static void setLog(HashMap<String, Object> logs) {

        log.add(logs);
    }

    public static ArrayList<HashMap<String, Object>> getLog() {
        return log;
    }
}
