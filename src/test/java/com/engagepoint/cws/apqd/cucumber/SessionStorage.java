package com.engagepoint.cws.apqd.cucumber;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStorage {

    public static final Map<String, Object> session = new ConcurrentHashMap<String, Object>();

    public static String getString(String key) {
        return (String) session.get(key);
    }
}
