package com.matrix.yukun.matrix.chat_module;

import android.util.Log;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.movie_module.util.ApiException;
import com.matrix.yukun.matrix.movie_module.util.MovieService;
import com.matrix.yukun.matrix.movie_module.util.RetrofitApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by yukun on 17-5-11.
 */
public class ChatPresent implements ChatControl.chatBasePresent {
    private ChatControl.View mView;
    private  String text ="";
    public ChatPresent(ChatControl.View view) {
        this.mView=view;
    }

    @Override
    public void getInfo(String msg) {
       getChatInfo(msg);

    }

    public void getChatInfo(String info){

        Retrofit retrofituil = RetrofitApi.getInstance().retrofitChat();
        Call<ChatInfo> chat = retrofituil.create(MovieService.class).getChat(info, AppConstants.ChatAppId);
        chat.enqueue(new Callback<ChatInfo>() {
            @Override
            public void onResponse(Call<ChatInfo> call, Response<ChatInfo> response) {
                ChatInfo body = response.body();
                if(null!=(body.getResult())){
                    text = body.getResult().getText();
                    //回调数据
                    mView.getMsg(text);
                }else {
                    if(body.getError_code()==10013){
                        mView.getMsg("不想和你说话了,我要睡觉了!晚安");
                        return;
                    }
                    mView.getMsg("请好好说话,好吗?我不喜欢图片和符号,要不然我要粘贴复制了哈!");
                }
            }

            @Override
            public void onFailure(Call<ChatInfo> call, Throwable t) {
                MyApp.showToast(t.toString()+"");
                mView.getMsg("不好意思,我出错了");
                new Throwable(new ApiException(1));
            }
        });
    }
}
