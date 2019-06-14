package com.matrix.yukun.matrix.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.matrix.yukun.matrix.constant.AppConstant;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

/**
 * Created by yukun on 17-1-25.
 */
public class FileUtil {

    private static FileInputStream in;
    private static FileOutputStream out;

    /**
     * 分享功能
     *            上下文
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public static Intent shareMsg(String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 创建File
     * @return
     */
    public static File createFile(){
        File destDir = new File(AppConstant.IMAGEPATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir;
    }
    /**
     * 创建File
     * @return
     */
    public static File createFile(String path){
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir;
    }

    /**
     * 写文件
     * @param filePath
     * @param fileName
     * @param content
     */
    public static void write(String filePath,String fileName,String content) {

        File file =new File(filePath,fileName);
        Writer out = null;
        try {
            out = new FileWriter(file);
            out.write(content);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读文件
     * @param filePath
     * @return
     */
    public static String read(String filePath) {
        String content="";
        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line;
            while ((line = br.readLine()) != null) {
                content+=line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * load
     * @param bm
     * @param photoName
     */
    public static void loadImage(Bitmap bm,String photoName){

        File destDir = createFile();

        File f = new File(destDir, photoName);
        if(f.exists()){
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 0, out); //0 压缩率 30 是压缩率，表示压缩70%;
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * load
     * @param bm
     * @param photoName
     */
    public static void loadTempImage(Bitmap bm,String path,String photoName){

        File destDir = new File(AppConstant.GIFPATH+"/"+path);
        if(!destDir.exists()){
            destDir.mkdirs();
        }
        File f = new File(destDir, photoName);
        if(f.exists()){
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 0, out); //0 压缩率 30 是压缩率，表示压缩70%;
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void showToast(Context context,int rotate){
        Toast toast = Toast.makeText(context,
                "色彩值升级为"+rotate+"，可以改变色彩了", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options;
        if(baos.toByteArray().length / 1024<6000){
            options = 100;
        }else {
            options = 30;
        }
        while (baos.toByteArray().length / 1024 > 400) { // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            Log.i("---options",options+"="+(baos.toByteArray().length / 1024));
            if(options>=20){
                options -= 10;// 每次都减少10
            }else if(options>=4){
                options-=2;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    //delete
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }


    /**
     * load
     * @param
     */
    public static void loadPath(String photoPath){
        File destDir = createFile();
        int byteread = 0; // 读取的字节数
        try {
            in = new FileInputStream(photoPath);
            out = new FileOutputStream(destDir);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String formatFileSize(long length) {
        String result = null;
        int sub_string = 0;
        // 如果文件长度大于1GB
        if (length >= 1073741824) {
            sub_string = String.valueOf((float) length / 1073741824).indexOf(
                    ".");
            result = ((float) length / 1073741824 + "000").substring(0,
                    sub_string + 3) + "GB";
        } else if (length >= 1048576) {
            // 如果文件长度大于1MB且小于1GB,substring(int beginIndex, int endIndex)
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0,
                    sub_string + 3) + "MB";
        } else if (length >= 1024) {
            // 如果文件长度大于1KB且小于1MB
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0,
                    sub_string + 3) + "KB";
        } else if (length < 1024)
            result = Long.toString(length) + "B";
        return result;
    }
    /**
     * 调用系统应用打开文件
     *
     * @param context
     * @param file
     */
    public static void openFile(Context context, File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(getFileUri(context, file), getMIMEType(file));
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            if (null == componentName) {
                Toast.makeText(context, "找不到打开此文件的应用！", Toast.LENGTH_SHORT).show();
            }
            else {
                context.startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(context, "找不到打开此文件的应用！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    /**
     * 获取文件Uri，兼容Android 7.0 及以上版本
     *
     * @param context
     * @param file
     *
     * @return
     */
    public static Uri getFileUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {    // Android 7.0 以上版本
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        }
        else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        String end = fName.substring(dotIndex, fName.length()).toLowerCase(Locale.CHINA);
        if ("".equals(end)) {
            return type;
        }
        for (String[] aMIME_MapTable : MIME_MapTable) {
            if (end.equals(aMIME_MapTable[0])) {
                type = aMIME_MapTable[1];
            }
        }
        return type;
    }

    /**
     * 建立一个MIME类型与文件后缀名的匹配表
     */
    private final static String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            { ".3gp", "video/3gpp" },   //
            { ".apk", "application/vnd.android.package-archive" },  //
            { ".asf", "video/x-ms-asf" },   //
            { ".avi", "video/x-msvideo" },  //
            { ".bin", "application/octet-stream" }, //
            { ".bmp", "image/bmp" }, //
            { ".c", "text/plain" }, //
            { ".class", "application/octet-stream" },   //
            { ".conf", "text/plain" }, //
            { ".cpp", "text/plain" }, //
            { ".doc", "application/msword" },   //
            { ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" }, //
            { ".exe", "application/octet-stream" }, //
            { ".gif", "image/gif" },    //
            { ".gtar", "application/x-gtar" },  //
            { ".gz", "application/x-gzip" },    //
            { ".h", "text/plain" }, //
            { ".htm", "text/html" },    //
            { ".html", "text/html" },   //
            { ".jar", "application/java-archive" }, //
            { ".java", "text/plain" },  //
            { ".jpeg", "image/jpeg" },  //
            { ".jpg", "image/jpeg" },   //
            { ".js", "application/x-javascript" },  //
            { ".log", "text/plain" },   //
            { ".m3u", "audio/x-mpegurl" },  //
            { ".m4a", "audio/mp4a-latm" },  //
            { ".m4b", "audio/mp4a-latm" },  //
            { ".m4p", "audio/mp4a-latm" }, //
            { ".m4u", "video/vnd.mpegurl" }, //
            { ".m4v", "video/x-m4v" }, //
            { ".mov", "video/quicktime" },  //
            { ".mp2", "audio/x-mpeg" }, //
            { ".mp3", "audio/x-mpeg" }, //
            { ".mp4", "video/mp4" }, //
            { ".mpc", "application/vnd.mpohun.certificate" }, //
            { ".mpe", "video/mpeg" },   //
            { ".mpeg", "video/mpeg" },  //
            { ".mpg", "video/mpeg" },   //
            { ".mpg4", "video/mp4" },   //
            { ".mpga", "audio/mpeg" },  //
            { ".msg", "application/vnd.ms-outlook" },   //
            { ".ogg", "audio/ogg" },    //
            { ".pdf", "application/pdf" },  //
            { ".png", "image/png" },    //
            { ".pps", "application/vnd.ms-powerpoint" },    //
            { ".ppt", "application/vnd.ms-powerpoint" },    //
            { ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" },   //
            { ".prop", "text/plain" },  //
            { ".rar", "application/x-rar-compressed" },    //
            { ".rc", "text/plain" },    //
            { ".rmvb", "audio/x-pn-realaudio" },    //
            { ".rtf", "application/rtf" },  //
            { ".sh", "text/plain" },    //
            { ".tar", "application/x-tar" },    //
            { ".tgz", "application/x-compressed" }, //
            { ".txt", "text/plain" },   //
            { ".wav", "audio/x-wav" },  //
            { ".wma", "audio/x-ms-wma" },   //
            { ".wmv", "audio/x-ms-wmv" },   //
            { ".wps", "application/vnd.ms-works" }, //
            { ".xls", "application/vnd.ms-excel" }, //
            { ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },   //
            { ".xml", "text/plain" },   //
            { ".z", "application/x-compress" }, //
            { ".zip", "application/zip" },  //
            { "", "*/*" }   //
    };
}
