package com.matrix.yukun.matrix.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.matrix.yukun.matrix.bean.AppConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        File destDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ AppConstants.PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir;
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

}
