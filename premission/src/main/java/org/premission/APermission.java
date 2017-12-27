package org.premission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;

import java.util.ArrayList;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/12/27.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class APermission {


    static final String TAG = "APermission";

    APremissionFragment mAPremissionFragment;
    ArrayList<String> mUnRequestPermissions;

    private APermission(Activity activity) {
        mAPremissionFragment = getRxPermissionsFragment(activity);
    }

    private APremissionFragment getRxPermissionsFragment(Activity activity) {
        APremissionFragment rxPermissionsFragment = findRxPermissionsFragment(activity);
        boolean isNewInstance = rxPermissionsFragment == null;
        if (isNewInstance) {
            rxPermissionsFragment = new APremissionFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(rxPermissionsFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return rxPermissionsFragment;
    }

    private APremissionFragment findRxPermissionsFragment(Activity activity) {
        return (APremissionFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    public static APermission with(Activity activity) {
        return new APermission(activity);
    }

    public APermission permission(String... permissions) {
        mUnRequestPermissions = new ArrayList<>();
        for (String requestPermission : permissions) {
            if (isGranted(requestPermission)) {
                continue;
            }
            if (isRevoked(requestPermission)) {
                continue;
            }
            mUnRequestPermissions.add(requestPermission);
        }
        return this;
    }

    public void request(Callback callback) {
        if (!isMarshmallow() || mUnRequestPermissions.isEmpty()) {
            // success
            callback.onNext(true);
        } else {
            String[] unrequestedPermissionsArray = mUnRequestPermissions.toArray(new String[mUnRequestPermissions.size()]);
            requestPermissionsFromFragment(unrequestedPermissionsArray, callback);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissionsFromFragment(String[] permissions, Callback callback) {
        mAPremissionFragment.requestPermissions(permissions,callback);
    }

    boolean isRevoked(String permission) {
        return isMarshmallow() && mAPremissionFragment.isRevoked(permission);
    }

    boolean isGranted(String permission) {
        return !isMarshmallow() || mAPremissionFragment.isGranted(permission);
    }

    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public interface Callback {

        void onNext(boolean isSuccess);
    }
}
