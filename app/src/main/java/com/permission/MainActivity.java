package com.permission;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import org.premission.*;


public class MainActivity extends Activity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        APermission.with(this)
                .permission(Manifest.permission.CAMERA)
                .request(new APermission.Callback() {
                    @Override
                    public void onNext(boolean isSuccess) {
                        Log.e(TAG, "onNext: " + isSuccess);
                    }
                });
    }
}
