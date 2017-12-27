package com.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/12/27.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class TestActivity extends Activity {

    public static void start(Context context, String... permission) {
        Intent intent = new Intent(context,TestActivity.class);
        intent.putExtra("permission",permission);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarTranslucent();

        String[] permission = getIntent().getStringArrayExtra("permission");

        ActivityCompat.requestPermissions(this, permission, 10);
    }

    /**
     * 状态栏透明,整个界面全屏
     */
    private void statusBarTranslucent() {
        // 代表 5.0 及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            return;
        }

        // versionCode > 4.4  and versionCode < 5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
