package com.example.john.se_registration.core;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.io.File;

/**
 * Created by john on 10/17/17.
 */

public class BaseApplication extends Application {
    private static BaseApplication thisInstance;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        thisInstance = this;
    }

    public static synchronized BaseApplication getInstance() {
        return thisInstance;
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else if(dir!= null && dir.isFile())
            return dir.delete();
        else {
            return false;
        }
    }
}