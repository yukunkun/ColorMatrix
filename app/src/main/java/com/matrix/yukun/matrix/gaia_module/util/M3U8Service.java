package com.matrix.yukun.matrix.gaia_module.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;


/**
 * Created by yukun on 16-11-16.
 */
public class M3U8Service extends MyNanoHTTPD{

    private static String TAG = "M3U8Service";

    private static M3U8Service server=null;

    public static  int PORT = 9080;
    private static ServerSocket serverTest;

    /**
     * 启动服务
     */
    public static void execute() {
        try {
            server = M3U8Service.class.newInstance();
            server.start(MyNanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException ioe) {
            Log.e(TAG, "启动服务失败：\n" + ioe);
//            System.gc();
//            System.exit(-1);
        } catch (Exception e) {
            Log.e(TAG, "启动服务失败：\n" + e);
//            System.gc();
//            System.exit(-1);
        }

        Log.i("------"+TAG, "服务启动成功\n");

        try {
            System.in.read();
        } catch (Throwable ignored) {

        }
    }
    //判断端口是否被占用
    public static boolean isPortAvailable(int port) {
        try {
            serverTest = new ServerSocket(port);
            serverTest.close();
            System.out.println("The port is available.");
            return true;
        } catch (IOException e) {
            PORT++;
            System.out.println("The port is occupied.");
        }
        return true;
    }

    /**
     * 关闭服务
     */
    public static void finish() {
        if(server != null){
            server.stop();
            Log.i(TAG, "服务已经关闭"+PORT);
            server = null;
        }else {
            Log.i(TAG, "服务为空,不用关闭"+PORT);
        }
    }

    public M3U8Service() {
        super(PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String url = String.valueOf(session.getUri());
        File file = new File(Environment.getExternalStorageDirectory()+url);
        Log.d(TAG, Environment.getExternalStorageDirectory()+url);
        if(file.exists()){
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "文件不存在：" + url);
            }
//
            long length = file.length();
            // ts文件
            String mimeType = "video/mpeg";
            if(url.contains(".m3u8")){
                // m3u8文件
                mimeType = "video/x-mpegURL";
            }
            return newChunkedResponse(Response.Status.OK, mimeType, fis);
//            return newChunkedResponse(Status.OK, "mine", fis);
        } else {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "文件不存在：" + url);
        }
    }

}
