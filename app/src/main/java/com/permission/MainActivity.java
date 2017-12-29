package com.permission;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.ok.http.HttpUtils;
import org.ok.http.engin.OkHttpEngin;
import org.ok.http.mode.callback.Callback;
import org.ok.http.mode.json.FastJsonParse;
import org.ok.log.L;
import org.premission.APermission;


public class MainActivity extends Activity {

    public Button btnClick;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpUtils.init(new OkHttpEngin());

        HttpUtils.initParse(new FastJsonParse());
        L.debug(true);
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

        HttpUtils.with(this)
                .url("https://www.baidu.com")
                .get(new Callback.CommonCallback<String>() {

                    @Override
                    public void onSuccess(String s, String s2) {
                        Toast.makeText(MainActivity.this, s + s2, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String s, Throwable throwable) {

                    }

                    @Override
                    public void onFinal(String s) {

                    }
                });
    }
}
