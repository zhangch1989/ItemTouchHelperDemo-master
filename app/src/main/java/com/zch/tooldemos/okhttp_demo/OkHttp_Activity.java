package com.zch.tooldemos.okhttp_demo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.zch.tooldemos.R;
import com.zch.tooldemos.tools.CommonUtils;
import com.zch.tooldemos.tools.ToastUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zch on 2019/3/25 15:22.
 */

public class OkHttp_Activity extends BaseRequestActivity implements MyCallBackListener{
    private final static String TAG = "OkHttp_Activity";
    private Context context;

    private Button btn_testhttp, btn_StringCallBack, btn_MapParams, btn_MapListener;
    private Button btn_downfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okhttp_activity);
        context = this;
        if (Build.VERSION.SDK_INT >= 23){
            CommonUtils.requestLocPermissions(this);
        }
        initView();
        initEvent();
    }

    private void initView(){
        btn_testhttp = (Button) findViewById(R.id.btn_testhttp);
        btn_StringCallBack = (Button) findViewById(R.id.btn_StringCallBack);
        btn_MapParams = (Button) findViewById(R.id.btn_MapParams);
        btn_MapListener = (Button) findViewById(R.id.btn_MapListener);
        btn_downfile = (Button) findViewById(R.id.btn_downfile);
    }

    private void initEvent(){
        btn_testhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData2();
            }

        });
        btn_StringCallBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        btn_MapParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData_Base();
            }
        });
        btn_MapListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData_2();
            }
        });
        btn_downfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downLoadFile();
            }
        });
    }

    /**
     * 使用StringCallBack
     */
    private void getData(){
        String url = "http://139.159.252.39:8080/p1upgrade/coordinateCollect/getPointInfo";
        OkHttpUtils.post().id(1).url(url)
                .addParams("areai", "500104")
                .addParams("token","123_grand")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "exception= " + e + " -- id=" + id);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "Response= " + response + " -- id=" + id);
            }
        });


    }

    /**
     * 使用原始的CallBack
     */
    private void getData2(){
        String url = "http://139.159.252.39:8080/p1upgrade/coordinateCollect/getPointInfo";
        OkHttpUtils.post().id(2).url(url).addParams("token","123_grand").build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                Log.e(TAG, " -- id=" + id +" ==Response= " + response.body().string() );
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "exception= " + e + " -- id=" + id);
            }

            @Override
            public void onResponse(Object response, int id) {
                Log.e(TAG, "id===" +id +" Response= " + response );
            }
        });
    }


    /**
     * 采用自定义的MyStringCallBack
     */
    private void getData_Base(){
        String url = "http://139.159.252.39:8080/p1upgrade/coordinateCollect/getPointInfo";
        Map<String, String> map = new HashMap<>();
        map.put("areaid", "500104");
        map.put("token", "123_grand");
        basePost(url, map, 0, new MyStringBack());
    }

    @Override
    public void onComplete(int code, String response) {
        Log.e(TAG, "code = "+ code + " complete =" + response);
    }

    @Override
    public void onError(int code, String response) {
        Log.e(TAG, "code= " + code + "  error = " +response);
    }

    /**
     * 自定义StringCallBack
     */
    public class MyStringBack extends StringCallback{

        @Override
        public void onError(Call call, Exception e, int id) {
            Log.e(TAG, "exception= " + e + " -- id=" + id);
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "Response= " + response + " -- id=" + id);
        }
    }

    /**
     * 采用接口监听的方式
     */
    private void getData_2(){
        String url = "http://139.159.252.39:8080/p1upgrade/coordinateCollect/getPointInfo";
        Map<String, String> map = new HashMap<>();
        map.put("areaid", "500104");
        map.put("token", "123_grand");
        basePost2(url, map, 4, this);
    }

    private void downLoadFile(){
        String fileurl = "http://139.159.252.39:8081/p1upgrade/picfiles/demo.mp4";
        String storpath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) +"/download";//文件下载的存储路径
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "download");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("download", "failed to create directory");
                return ;
            }
        }
        OkHttpUtils.get().url(fileurl).build().execute(new FileCallBack(storpath, "down.mp4") {
            @Override
            public void inProgress(float progress, long total, int id) {
//                super.inProgress(progress, total, id);
                Log.e(TAG, "curr progress ==" + progress);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError :" + e.getMessage());
            }

            @Override
            public void onResponse(File response, int id) {
                Log.e(TAG, "onResponse :" + response.getAbsolutePath());
                ToastUtils.showToast(context, "download success!");
            }
        });
    }

}
