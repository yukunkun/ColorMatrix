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
public class ChatNet {
    private static String text ="";
    public static String getChatInfo(String info){

        Retrofit retrofituil = RetrofitApi.getInstance().retrofitChat();
        Call<ChatInfo> chat = retrofituil.create(MovieService.class).getChat(info, AppConstants.ChatAppId);
        chat.enqueue(new Callback<ChatInfo>() {
            @Override
            public void onResponse(Call<ChatInfo> call, Response<ChatInfo> response) {
                ChatInfo body = response.body();
//                Log.i("----",body.toString());
                if(body.getResult().getText()!=null){
                    text = body.getResult().getText();
                }
            }

            @Override
            public void onFailure(Call<ChatInfo> call, Throwable t) {
                MyApp.showToast(t.toString()+"");
                new Throwable(new ApiException(1));
            }
        });
        return text;
    }
}
