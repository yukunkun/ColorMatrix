package com.matrix.yukun.matrix.leshilive_module.present;

import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.leshi_module.present.RetrofitInfo;
import com.matrix.yukun.matrix.movie_module.util.MovieService;
import com.matrix.yukun.matrix.movie_module.util.RetrofitApi;
import com.matrix.yukun.matrix.util.MD5Encoder;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yukun on 17-3-28.
 */
public class LiveInfo {

    public static Observable<Object> getLiveList(String method,String ver,String timestamp){
        Retrofit retrofit = RetrofitApi.getInstance().retrofitLiveUtil();
        String sign=null;
        String str="methodletv.rtmp.stream.searchtimestamp"+timestamp+"userid"+AppConstants.UserId+"ver"+ver;
        try {
            sign= MD5Encoder.encode(str+AppConstants.Secret_Key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofit.create(MovieService.class).getLiveList(method,ver,AppConstants.UserId,timestamp,sign)
                .filter(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(Object o) {
                        return null;
                    }
                });
    }

}
