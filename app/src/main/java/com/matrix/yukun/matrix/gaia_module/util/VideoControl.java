package com.matrix.yukun.matrix.gaia_module.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import okhttp3.Call;

/**
 * Created by yukun on 16-12-12.
 */
public class VideoControl {
    private String m3u8String;
    private DownloadTs downloadTs;
    private ParaTs paraTs;
    ArrayList<String> tsString=new ArrayList<>();
    ArrayList<String> names=new ArrayList<>();
    ArrayList<String> timeString=new ArrayList<>();
    ArrayList<String> timeAll;
    private ArrayList<Integer> downloadTag=new ArrayList<>();
    private Double maxTime=0.0;
    private int load_num=0;
    private int downLoadOver=3; //控制下载片数
    private int sprit_num=3; //当前的下载个数


    String format = String.format("http://localhost:%d", M3U8Service.PORT);
    String uri_1 = format+"/yukun/gaia/test.m3u8";
    private String videoRatio;

    public void setM3u8String(String m3u8String) {
        this.m3u8String = m3u8String;
        initString();
    }

    public ArrayList<String> getTsString() {
        return tsString;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public void setDownLoadOver(int downLoadOver) {
        this.downLoadOver = downLoadOver;
    }

    public void setLoad_num(int load_num) {
        this.load_num = load_num;
        sprit_num=load_num+3;
        downloadTag.clear();
        if(downloadTs!=null){
            downloadTs.stopOkHttpUtils();
        }
    }
    public void setResetKey() {
        downloadTs.setKey_byte(null);
    }

    public void setDownloadTag(int downloadNum) {
        downloadTag.add(downloadNum);
    }
    public void setDownloadStop() {
        if(downloadTs!=null){
            downloadTs.stopOkHttpUtils();
        }
    }

    public void initString(){
        //调用的时候,全部初始化
        load_num=0;
        sprit_num=3;
        downLoadOver=3;
        maxTime=0.0;
        downloadTs = new DownloadTs();
        downloadTs.startLoadCon();
        paraTs = new ParaTs();
        paraTs.setStrings(m3u8String);
        tsString = paraTs.returnTsUri();
        names = paraTs.returnTsName();
        timeString = paraTs.getTimes();
        timeAll = paraTs.getTimeAll();
        videoRatio = paraTs.getVideoRato();
//        paraTs.writeM3U8(paraTs.getHeadTs());
        for (int i = 0; i < timeAll.size(); i++) {
            try{
                maxTime = maxTime + Double.valueOf(timeAll.get(i));
            }catch (Exception e){
                ToastUtils.showToast("视频播放异常");
                e.printStackTrace();
                return;
            }
        }
        Log.i("----ts_number",timeAll.size()+"");
        load();
    }

    public void load(){
        ArrayList<String> strings1=new ArrayList<>();
        ArrayList<String> name1=new ArrayList<>();
        if(tsString.size()>load_num){  //一片
            for (int i = load_num; i < load_num+1; i++) {
                strings1.add(tsString.get(i));
                name1.add(names.get(i));
            }
        }
        if(load_num!=tsString.size()){  //有可下载的了
            downloadTs.setUri(strings1,name1,load_num);
            load_num++;
        }
    }

    public void writeHeader(){
        paraTs.writeM3U8(paraTs.getHeadTs());
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //ts拼接控制
                    Log.i("--------load_nun>3",load_num+"");
                    //添加Ts的头资源
                    paraTs.writeM3U8(paraTs.getHeadTs());
                    for (int i = 0; i < 3; i++) {
                        paraTs.splitTs(load_num-4+i);
                    }
                    getUri_1(uri_1);
                    EventBus.getDefault().post(new EventInitView(1));
                    handler.sendEmptyMessageDelayed(3,10*1000);
                    break;
                //ts只有两片
                case 2:
                    //添加Ts的头资源
                    paraTs.writeM3U8(paraTs.getHeadTs());
                    for (int i = 0; i < 2; i++) {
                        paraTs.splitTs(load_num-2+i);
                    }
                    getUri_1(uri_1);
                    EventBus.getDefault().post(new EventInitView(1));
//                    handler.sendEmptyMessageDelayed(3,10*1000);
                    break;
                case 3:
                //拼接之后的ts

                    Log.i("------downloadTag",downloadTag.size()+"+downLoadOver:"+downLoadOver+"+sprit_num:"+sprit_num);
                    if(downloadTag.size()>downLoadOver&&downloadTag.get(downLoadOver)==sprit_num){
                        paraTs.splitTs(sprit_num);  //拼接ts片
                        sprit_num++;
                        downLoadOver++;
                        getUri_1(uri_1);
                    }else {
                        Log.i("----initview","超时了");
    //                        Toast.makeText(VideoControl.this, "超时了", Toast.LENGTH_SHORT).show();
                        //超时了
                    }

                    if(sprit_num<tsString.size()){
                        handler.sendEmptyMessageDelayed(3,(long)((Math.floor(Double.valueOf(timeAll.get(sprit_num-1)))*10)*0.95)*100);
                    }
                break;
                //只有三片的
                case 4:
                    //添加Ts的头资源
                    paraTs.writeM3U8(paraTs.getHeadTs());
                    for (int i = 0; i < 3; i++) {
                        paraTs.splitTs(load_num-3+i);
                    }
                    getUri_1(uri_1);
                    EventBus.getDefault().post(new EventInitView(1));
                    break;
            }
        }
    };

    public void deletePlayedTs(int load_time){
        if(load_time<names.size()){
            FileDelete.delete(names.get(load_time));
        }
    }

    public void setHandlerStop(int con){
        if(con==1){
            handler.removeMessages(1);
        }else if(con==2){
            handler.removeMessages(2);
        }else if(con==3){
            handler.removeMessages(3);
        }

    }
    public void setHandlerStart(int con){
        if(con==1){
            handler.sendEmptyMessageDelayed(1,1);
        }else if(con==2){
            handler.sendEmptyMessageDelayed(2,1);
        }else if(con==3){
            handler.sendEmptyMessageDelayed(3,1);
        }
        else if(con==4){
            handler.sendEmptyMessageDelayed(4,1);
        }
    }
    public void setdownThreadCon(){
        if(downloadTs!=null){
            downloadTs.setThreadCon(false);
        }
    }

    private void getUri_1( String uri) {

        OkHttpUtils.get().url(uri).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("----srt2",response);
            }
        });
    }
}
