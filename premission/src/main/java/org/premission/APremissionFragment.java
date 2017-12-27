package org.premission;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/12/14.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class APremissionFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 42;
    private APermission.Callback mCallback;

    public APremissionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissions(@NonNull String[] permissions, APermission.Callback callback) {
        mCallback = callback;
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != PERMISSIONS_REQUEST_CODE) return;
        List<String> unrequestPermission = new ArrayList<>();
        for (String permission : permissions) {
            if (isGranted(permission)) {
                continue;
            }
            if (isRevoked(permission)) {
                continue;
            }
            unrequestPermission.add(permission);
        }
        mCallback.onNext(unrequestPermission.isEmpty());
    }


    @TargetApi(Build.VERSION_CODES.M)
    boolean isGranted(String permission) {
        return getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    boolean isRevoked(String permission) {
        return getActivity().getPackageManager().isPermissionRevokedByPolicy(permission, getActivity().getPackageName());
    }


}

