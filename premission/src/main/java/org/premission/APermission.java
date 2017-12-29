package org.premission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;

import java.util.ArrayList;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/27.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class APermission {


    static final String TAG = "APermission_APremissionFragment";

    APremissionFragment mAPremissionFragment;
    ArrayList<String> mUnRequestPermissions;

    private APermission(Activity activity) {
        mAPremissionFragment = getRxPermissionsFragment(activity);
    }

    private APremissionFragment getRxPermissionsFragment(Activity activity) {
        // 根据tag找Fragment
        APremissionFragment rxPermissionsFragment = findRxPermissionsFragment(activity);
        // 判断是否有此Activity
        boolean isNewInstance = rxPermissionsFragment == null;
        // 如果没有
        if (isNewInstance) {
            // 创建一个
            rxPermissionsFragment = new APremissionFragment();
            // 拿FragmentManager
            FragmentManager fragmentManager = activity.getFragmentManager();
            // 通过事物添加
            fragmentManager
                    .beginTransaction()
                    .add(rxPermissionsFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return rxPermissionsFragment;
    }

    // 通过tag找Fragment
    private APremissionFragment findRxPermissionsFragment(Activity activity) {
        return (APremissionFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    // 入口
    public static APermission with(Activity activity) {
        return new APermission(activity);
    }

    // 请求权限
    public APermission permission(String... permissions) {
        // 未申请的权限
        mUnRequestPermissions = new ArrayList<>();
        // 遍历权限
        for (String requestPermission : permissions) {
            if (isGranted(requestPermission)) {
                continue;
            }
            if (isRevoked(requestPermission)) {
                continue;
            }
            // 添加到未授权的集合
            mUnRequestPermissions.add(requestPermission);
        }
        return this;
    }

    // 开始请求
    public void request(Callback callback) {
        // 未给权限异常
        if (mUnRequestPermissions == null) {
            throw new NullPointerException("request permissions is null .");
        }
        // 是否6.0  或者没有请求权限
        if (!isMarshmallow() || mUnRequestPermissions.isEmpty()) {
            // success
            callback.onNext(true);
        } else {
            // 未申请的权限
            String[] unrequestedPermissionsArray = mUnRequestPermissions.toArray(new String[mUnRequestPermissions.size()]);
            // 申请
            requestPermissionsFromFragment(unrequestedPermissionsArray, callback);
        }
    }

    // 申请权限
    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissionsFromFragment(String[] permissions, Callback callback) {
        mAPremissionFragment.requestPermissions(permissions, callback);
    }

    // 查看权限是否符合policy的规定
    boolean isRevoked(String permission) {
        return isMarshmallow() && mAPremissionFragment.isRevoked(permission);
    }

    // 是否已经申请过
    boolean isGranted(String permission) {
        return !isMarshmallow() || mAPremissionFragment.isGranted(permission);
    }

    // 是否是6.0
    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public interface Callback {

        void onNext(boolean isSuccess);
    }
}
