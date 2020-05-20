package com.zch.tooldemos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.zch.tooldemos.demochannel.ChannelActivity;
import com.zch.tooldemos.demodrag.DragActivity;
import com.zch.tooldemos.demoviewpager.CircleViewPagerActivity;
import com.zch.tooldemos.demoviewpager.StandardViewPagerActivity;
import com.zch.tooldemos.document.Activity_Document;
import com.zch.tooldemos.doubleseekbar.Activity_DoubleSeekbar;
import com.zch.tooldemos.maplist.GaodeBottomSheetActivity;
import com.zch.tooldemos.okhttp_demo.OkHttp_Activity;
import com.zch.tooldemos.pageturn.Activity_PageTurn;
import com.zch.tooldemos.pic_turnpage.Activity_Pic_TurnPage;
import com.zch.tooldemos.silemenus.Activity_SlideMenus;
import com.zch.tooldemos.tools.ToastUtils;
import com.zch.tooldemos.uploadfile.UploadFile_Activity;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 *    weixin
 */
public class MainActivity extends Activity implements View.OnClickListener {


    private long firstTime = 0; //记录两次返回按钮的时间

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_drag).setOnClickListener(this);;
        findViewById(R.id.btn_channl).setOnClickListener(this);;
        findViewById(R.id.btn_viewpager).setOnClickListener(this);;
        findViewById(R.id.btn_viewpager2).setOnClickListener(this);;
        findViewById(R.id.btn_maplist).setOnClickListener(this);;
        findViewById(R.id.btn_okhttp).setOnClickListener(this);;
        findViewById(R.id.btn_uploadfile).setOnClickListener(this);;
        findViewById(R.id.btn_share).setOnClickListener(this);;
        findViewById(R.id.btn_seekbar).setOnClickListener(this);;
        findViewById(R.id.btn_slidemenus).setOnClickListener(this);
        findViewById(R.id.btn_glide).setOnClickListener(this);
        findViewById(R.id.btn_pageturn).setOnClickListener(this);
        findViewById(R.id.btn_picpageturn).setOnClickListener(this);
        findViewById(R.id.btn_document).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_drag:
                startActivity(new Intent(MainActivity.this, DragActivity.class));
                break;
            case R.id.btn_channl:
                startActivity(new Intent(MainActivity.this, ChannelActivity.class));
                break;
            case R.id.btn_viewpager:
                startActivity(new Intent(MainActivity.this, CircleViewPagerActivity.class));
                break;
            case R.id.btn_viewpager2:
                startActivity(new Intent(MainActivity.this, StandardViewPagerActivity.class));
                break;
            case R.id.btn_maplist:
                startActivity(new Intent(MainActivity.this, GaodeBottomSheetActivity.class));
                break;
            case R.id.btn_okhttp:
                startActivity(new Intent(MainActivity.this, OkHttp_Activity.class));
                break;
            case R.id.btn_uploadfile:
                startActivity(new Intent(MainActivity.this, UploadFile_Activity.class));
                break;
            case R.id.btn_share:
                showShare();
                break;
            case R.id.btn_seekbar:
                startActivity(new Intent(MainActivity.this, Activity_DoubleSeekbar.class));
                break;
            case R.id.btn_slidemenus:
                startActivity(new Intent(MainActivity.this, Activity_SlideMenus.class));
                break;
            case R.id.btn_glide:

                break;
            case R.id.btn_pageturn:
                startActivity(new Intent(MainActivity.this, Activity_PageTurn.class));
                break;
            case R.id.btn_picpageturn:
                startActivity(new Intent(MainActivity.this, Activity_Pic_TurnPage.class));
                break;
            case R.id.btn_document:
                startActivity(new Intent(MainActivity.this, Activity_Document.class));
                break;
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("分享");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (System.currentTimeMillis() - firstTime > 2000){
                ToastUtils.showToast(this, "再按一次退出程序");
                firstTime = System.currentTimeMillis();
            }else {
                finish();
                System.exit(0);
            }
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
