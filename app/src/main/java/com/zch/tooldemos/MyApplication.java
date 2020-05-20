package com.zch.tooldemos;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Zch on 2019/3/22 11:02.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        /**
         * 初始化ShareSDK
         */
        MobSDK.init(this);

        /**
         * 初始化okhttp
         */
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);


    }

    public static Context getContext(){
        return context;
    }
}
