package com.zch.tooldemos.okhttp_demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by Zch on 2019/3/25 17:01.
 */

public class BaseRequestActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void basePost(String url, Map<String, String> params, int code, StringCallback callback){

        OkHttpUtils.post().id(code).params(params).url(url).build().execute(callback);

    }
    public void basePost2(String url, Map<String, String> params, int code, final MyCallBackListener callback){

        OkHttpUtils.post().id(code).params(params).url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(id, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                callback.onComplete(id, response.toString());
            }
        });

    }
}
