package com.matrix.yukun.matrix.tool_module.qrcode.cropper;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FileDownloadUtil {
	/**
	 *
	 * @return
	 */
	public static String getDefaultLocalDir(String subDir) {

		String path_root = getSDcardRoot();
		if (path_root == null) {
			return null;
		}

		String path_dir = path_root + subDir;

		return makeDir(path_dir);
	}
	public static String getSDcardRoot() {

		if (!StorageUtil.isExternalStorageAvailable()) {
			return null;
		}
		File root = Environment.getExternalStorageDirectory();
		String path_root = root.getAbsolutePath();

		return path_root;
	}
	public static String makeDir(String path_dir) {
		File dir = new File(path_dir);
		if (!dir.exists()) {
			boolean success = dir.mkdirs();
			if (!success) {
				Log.e("test", path_dir);
				return null;
			} else {
				Log.i("test",  path_dir);
			}
		}

		return path_dir;
	}
}
