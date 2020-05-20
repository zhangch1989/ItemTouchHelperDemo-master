package com.zch.tooldemos.silemenus;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by Zch on 2019/4/18 10:30.
 * 侧滑界面
 */

public class SlideMainView extends HorizontalScrollView {
    private final static String TAG = "SlideMainView";

    //屏幕宽度
    private int mScreenWidth;

    //划出的菜单
    private ViewGroup mMenu;

    //主界面
    private ViewGroup mMain;

    private int mMenuRightPadding = 100;

    private int mMenuWidth;

    private boolean isInit = true;

    private boolean isOpen = false;

    public SlideMainView(Context context, AttributeSet attrs) {
        super(context, attrs);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;

        setHorizontalScrollBarEnabled(false);//去除滚动条
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isInit) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);

            mMenu = (ViewGroup) wrapper.getChildAt(0);
            mMain = (ViewGroup) wrapper.getChildAt(1);

            mMenuWidth = mScreenWidth - mMenuRightPadding; //设定菜单的宽度

            mMenu.getLayoutParams().width = mMenuWidth;
            mMain.getLayoutParams().width = mScreenWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            isInit = false;
        }
        scrollTo(mMenuWidth, 0);
        super.onLayout(changed, l, t, r, b);
    }

    private float downX;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                //抬起时 滑动的距离
                float dx = ev.getX() - downX;

                //如果滑动的距离小于屏幕宽度的1/3
                if (dx < mScreenWidth / 3) {
                    smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else {
                    smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
            default:
                break;

        }
        return super.onTouchEvent(ev);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        //滑动的百分比
        float factor = (float) l / mMenuWidth;

        //平移
        mMenu.setTranslationX(mMenuWidth * factor * 0.6f);

        //缩放
        float menuScale = 1 - 0.4f * factor;
//        mMenu.setScaleX(menuScale);   //侧滑菜单缩放，打开即有缩放效果
//        mMenu.setScaleY(menuScale);

        //透明度
//        mMenu.setAlpha(1 - factor);
    }

    /**
     * 关闭侧滑
     */
    public void closeMenus(){
        if(isOpen){
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    /**
     * 打开侧滑
     */
    public void openMenus(){
        if(!isOpen){
            this.smoothScrollTo(0, 0);
            isOpen = true;
        }
    }

}
