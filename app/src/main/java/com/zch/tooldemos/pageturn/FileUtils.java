package com.zch.tooldemos.pageturn;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Zch on 2020/4/23 15:56.
 */

public class FileUtils {

    public static String filepath = Environment.getExternalStorageDirectory() + "/PicShow/";

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     */
    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                if (!file.exists())
                    file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    if (!oldPath.equals("")) { // assets 文件夹下的目录
                        copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                    }else { // assets 文件夹
                        copyFilesFassets(context, fileName, newPath + "/" + fileName);
                    }
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程

        }
    }

    /**
     * 根据地址获取文件名称
     * @param url
     * @return
     */
    private String parseName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }

    public static Bitmap getImageFromAssetsFile(Context context, String fileName)
    {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try{
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("-----", e.toString());
        }
        return image;
    }

    public static Bitmap scaleBitmap(Bitmap btmap, int newWidth, int newHeight){
        if (btmap == null){
            return btmap;
        }
        int width = btmap.getWidth();
        int height = btmap.getHeight();
        // 设置想要的大小
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 修改得到的bitmap的大小,根据个人需求自行设置
        Bitmap bitMap = Bitmap.createBitmap(btmap, 0, 0, width, height, matrix, true);
        return bitMap;
    }

}
