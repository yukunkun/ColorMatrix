package com.matrix.yukun.matrix.tool_module.qrcode.cropper;

public class StorageUtil {
	/**
	 *
	 * @return
	 */
	public static boolean isExternalStorageAvailable() {
		return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
	}
}
