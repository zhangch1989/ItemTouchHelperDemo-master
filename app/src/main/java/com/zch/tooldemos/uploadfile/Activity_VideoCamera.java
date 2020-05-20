package com.zch.tooldemos.uploadfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.zch.tooldemos.R;
import com.zch.tooldemos.tools.CommonUtils;
import com.zch.tooldemos.tools.ToastUtils;

/**
 * Created by zch on 2018/6/26.
 */
public class Activity_VideoCamera extends Activity {
    private Camera mCamera;
    private CameraPreviewView previewView;
    private MediaRecorder recorder;
    private boolean isrecording = false;
    private Button btn, btn_cancel, btn_sure;
    private TextView tv_time;
    Handler startTimehandler;
    private long baseTimer;
    private Timer timer;

    private String result_path ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (Build.VERSION.SDK_INT >= 23){
            CommonUtils.requestLocPermissions(this);
            CommonUtils.requestPermissions(this);
            CommonUtils.requestVideoPermissions(this);
        }


        if (!checkCameraHardware(this)) {
            return;
        };
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        previewView = new CameraPreviewView(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(previewView);
        btn = (Button) findViewById(R.id.btn);
        btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        tv_time = (TextView) findViewById(R.id.tv_time);
        initEvent();

        startTimehandler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                 if (null != tv_time) {
                    tv_time.setText("录制时间：" + (String) msg.obj);
                 }
            }
        };
    }

    private void initEvent(){
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isrecording) {
                    releaseMediaRecoder();
                    //mCamera.lock();
                    btn.setText("拍摄");
                    isrecording = false;
                    timer.cancel();
                }
                else {
                    if (prepareVideoRecorder()) {
                        recorder.start();
                        btn.setText("Stop");
                        isrecording = true;
                        tv_time.setText("录制时间：00:00:00");
                        startTimer();
                    }
                    else {
                        releaseMediaRecoder();
                        timer.cancel();
                    }
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File file = new File(result_path);
                    file.delete();
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("path", result_path);
                Activity_VideoCamera.this.setResult(RESULT_OK, intent);
                Activity_VideoCamera.this.finish();
            }
        });
    }

    public void startTimer(){
        baseTimer = SystemClock.elapsedRealtime();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int time = (int)((SystemClock.elapsedRealtime() - baseTimer) / 1000);
                String hh = new DecimalFormat("00").format(time / 3600);
                String mm = new DecimalFormat("00").format(time % 3600 / 60);
                String ss = new DecimalFormat("00").format(time % 60);
                String timeFormat = new String(hh + ":" + mm + ":" + ss);
                Message msg = new Message();
                msg.obj = timeFormat;
                startTimehandler.sendMessage(msg);
            }

        }, 0, 1000L);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecoder();
        releaseCamera();
    }

    private void releaseMediaRecoder(){
        if (recorder != null) {
            recorder.reset();
            recorder.release();
            recorder = null;
            mCamera.lock();
        }
    }

    private void releaseCamera(){
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 创建MediaRecorder实例，并为之设定基本属性
     * @return
     */
    private boolean prepareVideoRecorder(){
        try {
            recorder = new MediaRecorder();
            mCamera.unlock();
            recorder.setCamera(mCamera);

            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);

//            recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

            // Set output file format
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            // 这两项需要放在setOutputFormat之后
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

            recorder.setVideoSize(640, 480);
            recorder.setVideoFrameRate(30);
            recorder.setVideoEncodingBitRate(3 * 1024 * 1024);
            recorder.setOrientationHint(90);
            //设置记录会话的最大持续时间（毫秒）
            recorder.setMaxDuration(30 * 1000);

            recorder.setPreviewDisplay(previewView.getHolder().getSurface());
            result_path = getOutputMediaPath();
            recorder.setOutputFile(result_path);
            recorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 获取输出video文件目录
     * @return
     */
    private String getOutputMediaPath() {
        java.util.Date date = new java.util.Date();
        String timeTemp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        Log.e("Error", "getOutputMediaPath file path---"+
                mediaStorageDir.getPath() + File.separator +
                "VID_" + timeTemp+ ".mp4");
        return mediaStorageDir.getPath() + File.separator +
                "VID_" + timeTemp + ".mp4";
    }

    public class CameraPreviewView extends SurfaceView implements SurfaceHolder.Callback{
        private SurfaceHolder holder;

        public CameraPreviewView(Context context, Camera camera) {
            super(context);

            holder = getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (holder.getSurface() == null) {
                return;
            }

            try {
                if(mCamera != null) {
                    holder.removeCallback(this);
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    mCamera.lock();
                    mCamera.release();
                    mCamera = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 获取camera实例
     * @return
     */
    public static Camera getCameraInstance(){
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * 检测手机有无摄像头
     * @param context
     * @return
     */
    private boolean checkCameraHardware(Context context){
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            ToastUtils.showToast(context, "successfully detact camera!");
            return true;
        }
        else {
            ToastUtils.showToast(context, "not detact camera!!!");
            return false;
        }
    }


}
