package com.zch.tooldemos.uploadfile;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;

import com.zch.tooldemos.R;
import com.zch.tooldemos.tools.CommonUtils;
import com.zch.tooldemos.tools.FileUtil;
import com.zch.tooldemos.tools.ToastUtils;
import com.zch.tooldemos.uploadfile.audio.RecordAudioDialogFragment;
import okhttp3.Call;

/**
 * Created by Zch on 2019/3/27 9:24.
 */

public class UploadFile_Activity extends FragmentActivity {
    private final static String TAG = "UploadFile_Activity";
    private Context context;

    private LinearLayout ll_pics;
    private ImageView ibtn_add;

    private File file;

    public static ReceiveHandler receiveHandler;
    public final static int HANDLE_UPLOAD_IMG = 1;
    public final static int HANDLE_RECORD_SEND = 2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadfile_acitivity);
        context = this;
        receiveHandler = new ReceiveHandler();
        if (Build.VERSION.SDK_INT >= 23){
            CommonUtils.requestLocPermissions(this);
            CommonUtils.requestVideoPermissions(this);
        }

        initView();
        initEvent();
    }

    private void initView(){
        ll_pics = (LinearLayout) findViewById(R.id.ll_pics);
        ibtn_add = (ImageView) findViewById(R.id.ibtn_add);
    }

    private void initEvent(){
        ibtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePic();
            }
        });
    }



    private void choosePic(){
        new AlertDialog.Builder(this)
                .setTitle("选择图片")
                .setItems(new String[]{"从相册选取", "拍照", "录音", "视频拍摄"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (0 == i){
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, 0);
                                }else if(1 == i){
//                                    file = new File(FileUtil.RequestCamera(context,1,""));
                                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(it, 1);
                                }else if(2 == i){
                                    final RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
                                    fragment.show(getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
                                    fragment.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                                        @Override
                                        public void onCancel() {
                                            fragment.dismiss();
                                        }
                                    });
                                }
                                else{
//                                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                                    //设置视频录制的最长时间
//                                    intent.putExtra (MediaStore.EXTRA_DURATION_LIMIT,30);
//                                    //设置视频录制的画质
//                                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                                    startActivityForResult (intent, 11);
                                    Intent it = new Intent(context, Activity_VideoCamera.class);
                                    startActivityForResult(it, 11);
                                }
                            }
                        }).setNegativeButton("取消", null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bitmap photo = null;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    context.getResources().getDimensionPixelSize(R.dimen.detail_pic_width),
                    context.getResources().getDimensionPixelSize(R.dimen.detail_pic_height));
            layoutParams.setMargins(0,0,10,0);
            if (requestCode == 1) {
                //拍照
                if (null != data) {
                    Bundle extras = data.getExtras();
                    if (extras == null) {
                        return;
                    }
                    final Bitmap b = (Bitmap) extras.get("data");
                    if (null != b) {
                        //显示图片
                        ImageView imageView = new ImageView(context);
                        imageView.setLayoutParams(layoutParams);  //设置图片宽高
                        imageView.setImageBitmap(b); //图片资源
                        ll_pics.addView(imageView); //动态添加图片

                        //更新到相册
                        file = new File(FileUtil.compressFile(b, ""));
                        FileUtil.sendBroad(file, context);
                        //以下执行上传
//                        receiveHandler.sendEmptyMessage(HANDLE_UPLOAD_IMG);
                    }
                }
            } else if (0 == requestCode) {
                //从相册获取图片
                if (null != data) {
                    try {
                        ContentResolver cr = this.getContentResolver();
                        Uri uri = data.getData();
                        final Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        if (null != bitmap) {
                            //显示图片
                            ImageView imageView = new ImageView(context);
                            imageView.setLayoutParams(layoutParams);  //设置图片宽高
                            imageView.setImageBitmap(bitmap); //图片资源
                            ll_pics.addView(imageView); //动态添加图片
                        }
                        //以下执行上传
                        file = new File(FileUtil.compressFile(bitmap, ""));
//                        receiveHandler.sendEmptyMessage(HANDLE_UPLOAD_IMG);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showToast(context, "未选择图片");
                }
            } else if (requestCode == 11) { //视频录制
                if (data != null) {
                    final String filepath = data.getExtras().getString("path");
                    final VideoView videoView = new VideoView(context);
                    Uri uri = Uri.parse(filepath);
                    videoView.setLayoutParams(layoutParams);
                    videoView.setVideoURI(uri);
                    videoView.setMediaController(new MediaController(context));
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
//                            videoView.start();
                        }
                    });
                    ll_pics.addView(videoView,ll_pics.getChildCount() - 1);
                    Log.e(TAG, "onActivityResult: " + filepath);
                    file = new File(filepath);
                    Log.e(TAG, "filename----"+ file.getName());
//                    receiveHandler.sendEmptyMessage(HANDLE_UPLOAD_IMG);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void upload(){
        String url = "http://139.159.252.39:8080/p1upgrade/app/api/fileUpload";
        OkHttpUtils.post().url(url).addParams("token", "123_grand").addFile("myfile", file.getName(), file).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "exception ===" +e);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "url===="+ response);
            }
        });
    }

    public class ReceiveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLE_UPLOAD_IMG:
                    upload();
                    break;
                case HANDLE_RECORD_SEND:
                    String path = msg.getData().getString("filepath");
                    final VideoView videoView = new VideoView(context);
                    Uri uri = Uri.parse(path);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            context.getResources().getDimensionPixelSize(R.dimen.detail_pic_width),
                            context.getResources().getDimensionPixelSize(R.dimen.detail_pic_height));
                    layoutParams.setMargins(0,0,10,0);
                    videoView.setLayoutParams(layoutParams);
                    videoView.setVideoURI(uri);
                    videoView.setMediaController(new MediaController(context));
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
//                            videoView.start();
                        }
                    });
                    ll_pics.addView(videoView,ll_pics.getChildCount() - 1);
                    file = new File(path);
//                    receiveHandler.sendEmptyMessage(HANDLE_UPLOAD_IMG);
                    break;

            }
        }
    }

}
