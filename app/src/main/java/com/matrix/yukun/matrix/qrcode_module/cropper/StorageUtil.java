package com.matrix.yukun.matrix.qrcode_module.cropper;

public class StorageUtil {
	/**
	 *
	 * @return
	 */
	public static boolean isExternalStorageAvailable() {
		return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
	}
}
