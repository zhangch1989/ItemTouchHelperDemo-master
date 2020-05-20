package com.zch.tooldemos.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Zch on 2020/1/13 14:57.
 */

public class ImageLoader {
    private static ImageLoader imageLoader;

    public static ImageLoader getInstance() {
        if (imageLoader == null){
            imageLoader = new ImageLoader();
        }
        return imageLoader;
    }

    /**
     * 加载网络图片
     * @param context 上下文
     * @param imgUrl 图片地址
     * @param defaultPic 占位图
     * @param errorPic 错误加载图
     */
    public void loadByUrl(Context context, String imgUrl, int defaultPic, int errorPic, ImageView imageView){
        final RequestOptions options = new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.priority(Priority.HIGH);
        options.error(errorPic);
        options.placeholder(defaultPic);
        Glide.with(context).load(imgUrl).apply(options).into(imageView);
    }
    public void loadByUrl(Context context, String imgUrl, int defaultPic, ImageView imageView){
        final RequestOptions options = new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.priority(Priority.HIGH);
        options.error(defaultPic);
        options.placeholder(defaultPic);
        Glide.with(context).load(imgUrl).apply(options).into(imageView);
    }
    /**
     * 加载圆角图片
     * @param context
     * @param imgUrl
     * @param round
     * @param imageView
     */
    public void loadCornerImage(Context context,String imgUrl,int round,ImageView imageView){
        Glide.with(context)
                .load(imgUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(round)))//圆角半径
                .into(imageView);
    }

    /**
     * 加载圆形图片
     * @param context
     * @param imgUrl
     * @param imageView
     */
    public void loadRoundImage(Context context,String imgUrl,ImageView imageView){
        Glide.with(context)
                .load(imgUrl)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }

    /**
     * 加载资源图片
     * @param context 上下文
     * @param imgResource 资源图片
     * @param defaultPic 占位图
     * @param errorPic 错误加载图
     */
    public void loadImage(Context context, int imgResource, int defaultPic,int errorPic,ImageView imageView){
        final RequestOptions options = new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.priority(Priority.HIGH);
        options.error(errorPic);
        options.placeholder(defaultPic);
        Glide.with(context).load(imgResource).apply(options).into(imageView);
    }

}
