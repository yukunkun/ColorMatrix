package com.matrix.yukun.matrix.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.SplashActivity;

/**
 * Created by Administrator on 2017/2/5.
 */
public class DeskMapUtil {

    public static void createShortCut(Context context) {
        // 先判断该快捷是否存在
        if (!isExist(context)) {
            Intent intent = new Intent();
            // 指定动作名称
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            // 指定快捷方式的图标
            Parcelable icon = Intent.ShortcutIconResource.fromContext(context,R.mipmap.tool_icon);
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            // 指定快捷方式的名称
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Matrix");
            // 指定快捷图标激活哪个activity
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName component = new ComponentName(context, SplashActivity.class);
            i.setComponent(component);
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
            context.sendBroadcast(intent);
        }
    }

    private static boolean isExist(Context context) {
        boolean isExist = false;
        int version = getSdkVersion();
        Uri uri = null;
        if (version < 2.0) {
            uri = Uri.parse("content://com.android.launcher.settings/favorites");
        } else {
            uri = Uri.parse("content://com.android.launcher2.settings/favorites");
        }
        String selection = " title = ?";
        String[] selectionArgs = new String[] { "Matrix" };
        Cursor c = context.getContentResolver().query(uri, null, selection, selectionArgs, null);

        if (c != null && c.getCount() > 0) {
            isExist = true;
        }else {
            isExist=false;
        }

        if (c != null) {
            c.close();
        }

        return isExist;
    }
    /**
     * 得到当前系统SDK版本
     */
    private static int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

}
