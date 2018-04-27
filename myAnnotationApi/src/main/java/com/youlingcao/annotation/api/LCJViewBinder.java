package com.youlingcao.annotation.api;

import android.app.Activity;

import java.util.LinkedHashMap;
import java.util.Map;

public class LCJViewBinder {
    private static final ActivityViewFinder activityFinder = new ActivityViewFinder();
    private static final Map<String, ViewBinder> binderMap = new LinkedHashMap<>();

    public static void bind(Activity activity) {
        bind(activity, activity, activityFinder);
    }

    public static void bind(Object host, Object object, ViewFinder finder) {
        String className = host.getClass().getName();
        ViewBinder binder = binderMap.get(className);
        if (binder == null) {
            try {
                Class<?> aClass = Class.forName(className + "$$ViewBinder");
                binder = (ViewBinder) aClass.newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        if (binder != null) {
            binder.bindView(host, object, finder);
        }
    }

    public static void unBind(Object host) {
        String className = host.getClass().getName();
        ViewBinder binder = binderMap.get(className);
        if (binder != null) {
            binder.unBindView(host);
        }
        binderMap.remove(className);
    }
}
