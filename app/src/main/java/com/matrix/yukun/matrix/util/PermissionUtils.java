package com.matrix.yukun.matrix.util;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


import java.util.ArrayList;
import java.util.List;

/**
 * author: kun .
 * date:   On 2019/2/19
 */
public class PermissionUtils {

    static List<String> mPermissions;
    private static PermissionUtils mPermissionUtils=new PermissionUtils();
    public static int REQUESTCODE=1;
    private Activity mContext;

    public static PermissionUtils getInstance(){
        if(mPermissionUtils==null){
            mPermissionUtils=new PermissionUtils();
        }
        return mPermissionUtils;
    }

    public void setContext(Activity context) {
        mContext = context;
    }

    public List<String> setPermission(List<String> permissions){
        mPermissions=new ArrayList<>();
        checkPermission(permissions);
        return mPermissions;
    }

    public void start(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(mPermissions.size()>0){
                String[] permissings=mPermissions.toArray(new String[mPermissions.size()]);
                ActivityCompat.requestPermissions(mContext,permissings,REQUESTCODE);
            }
        }
    }

    private void checkPermission(List<String> permissions) {
        for (int i = 0; i < permissions.size(); i++) {
            if (ContextCompat.checkSelfPermission(mContext, permissions.get(i)) != PackageManager.PERMISSION_GRANTED){
                mPermissions.add(permissions.get(i));
            }
        }
    }
}
