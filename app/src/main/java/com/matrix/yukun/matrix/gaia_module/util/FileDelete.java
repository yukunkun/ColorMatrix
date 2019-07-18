package com.matrix.yukun.matrix.gaia_module.util;

import android.os.Environment;
import android.util.Log;
import com.matrix.yukun.matrix.AppConstant;
import java.io.File;

/**
 * Created by yukun on 16-11-29.
 */
public class FileDelete {
    //删除所有文件
    public static void deleteAll(){
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+AppConstant.M3U8);
        if (file.isDirectory()) {// 处理目录
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {

                if(!file.renameTo(file)) {
                    System.out.println("正在被别人使用中.");
                }else {
                    files[i].delete();
                }
                Log.i("------deleteAll",files[i]+"");
            }
        }
    }

    //删除单个文件
    public static void delete(String path){
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+AppConstant.M3U8+"/"+path.trim());
        if(file.exists()){       // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                boolean delete = file.delete(); // delete()方法 你应该知道是删除的意思;
            }
        }
    }
}
