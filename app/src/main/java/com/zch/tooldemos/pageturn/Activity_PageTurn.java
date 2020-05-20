package com.zch.tooldemos.pageturn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zch.tooldemos.MainActivity;
import com.zch.tooldemos.R;

/**
 * Created by Zch on 2020/4/28 9:20.
 * 图片翻页效果
 */

public class Activity_PageTurn extends Activity{
    private Context context;

    private LinearLayout linearLayout;
    private Pager pager;
    private PagerFactory pagerFactory;
    private Bitmap currentBitmap, mCurPageBitmap, mNextPageBitmap;
    private Canvas mCurPageCanvas, mNextPageCanvas;
    //图片数组
//    private static final String[] pages = {"http://pic.58pic.com/58pic/15/62/55/37Z58PICmtR_1024.jpg","http://pic1.16pic.com/00/03/24/16pic_324498_b.jpg","http://pic2.cxtuku.com/00/15/88/b305b7673527.jpg","http://pic18.nipic.com/20120105/293411_145330148396_2.jpg", "http://pic36.nipic.com/20131224/9904897_144358081000_2.jpg", "http://img2.makepolo.net/images/formals/img/product/430/174/6356faae42f9a838bfc036bb49d74656.jpg"};
    private static final String[] pages = {"1.png","2.png","3.png","4.png"};
    private int screenWidth;
    private int screenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pageturn);
        context = this;
        //初始化布局;
        linearLayout = (LinearLayout) findViewById(R.id.typeview);
        //初始化自定义组件
        initView();
        //默认加载第一张图
        loadImage(mNextPageCanvas, 0);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        pager = new Pager(this, screenWidth, screenHeight);
        //下面这段代码是全屏的,不太方便使用,注掉
/*        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(pager, layoutParams);*/
        //把咱们的图片加入到之前的布局里
        linearLayout.addView(pager);
        //图片类型
        mCurPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mCurPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);
        pagerFactory = new PagerFactory(context);
        pager.setBitmaps(mCurPageBitmap, mCurPageBitmap);
        //翻页监听
        pager.setOnTouchListener(new View.OnTouchListener() {
            private int count = pages.length;
            private int currentIndex = 0;
            private int lastIndex = 0;
            private Bitmap lastBitmap = null;
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                boolean ret = false;
                if (v == pager) {
                    if (e.getAction() == MotionEvent.ACTION_DOWN) {
                        pager.calcCornerXY(e.getX(), e.getY());
                        lastBitmap = currentBitmap;
                        lastIndex = currentIndex;
                        pagerFactory.onDraw(mCurPageCanvas, currentBitmap);
                        if (pager.DragToRight()) {    // 向右滑动，显示前一页
                            if (currentIndex == 0) return false;
                            pager.abortAnimation();
                            currentIndex--;
                            loadImage(mNextPageCanvas, currentIndex);
                            Toast.makeText(context, "当前第"+currentIndex+"页", Toast.LENGTH_SHORT).show();
                        } else {        // 向左滑动，显示后一页
                            if (currentIndex + 1 == count) return false;
                            pager.abortAnimation();
                            currentIndex++;
                            //加载图片并显示出来
                            loadImage(mNextPageCanvas, currentIndex);
                            Toast.makeText(context, "当前第"+currentIndex+"页", Toast.LENGTH_SHORT).show();
                        }
                    } else if (e.getAction() == MotionEvent.ACTION_MOVE) {

                    } else if (e.getAction() == MotionEvent.ACTION_UP) {
                        if (!pager.canDragOver()) {
                            currentIndex = lastIndex;
                            currentBitmap = lastBitmap;
                        }
                    }
                    ret = pager.doTouchEvent(e);
                    return ret;
                }
                return false;
            }
        });
    }

    /**
     * 请求图片并显示
     * @param canvas
     * @param index
     */
    private void loadImage(final Canvas canvas, int index) {
        try {
            Bitmap btmap = FileUtils.getImageFromAssetsFile(context, pages[index]);
            if (btmap == null){
                return;
            }

            int width = btmap.getWidth();
            int height = btmap.getHeight();
            // 设置想要的大小
            int newWidth = linearLayout.getWidth() == 0 ? 1000 : linearLayout.getWidth();
            int newHeight = linearLayout.getHeight() == 0 ? 400 : linearLayout.getHeight();
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 修改得到的bitmap的大小,根据个人需求自行设置
            Bitmap bitMap = Bitmap.createBitmap(btmap, 0, 0, width, height, matrix, true);
            currentBitmap = bitMap;
            //传入得到的图片
            pagerFactory.onDraw(canvas, bitMap);
            pager.setBitmaps(mCurPageBitmap, mNextPageBitmap);
            pager.postInvalidate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
