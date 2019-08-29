package com.matrix.yukun.matrix.util.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * author: kun .
 * date:   On 2019/8/29
 */
public class GlideUtil {

    private static Context context;

    static {
        context= MyApp.myApp;
    }

    public static void loadImage(String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }

    public static void loadPlaceholderImage(String url, ImageView imageView){
        Glide.with(context).load(url).apply(getOptions()).into(imageView);
    }

    public static void loadCircleImage(String url, ImageView imageView){
        Glide.with(context).load(url).apply(getCircleOptions()).into(imageView);
    }

    public static void loadBlurImage(String url, ImageView imageView){
        Glide.with(context).load(url).apply(getBlurOptions()).into(imageView);
    }

    public static void loadOptionsImage(String url, ImageView imageView,RequestOptions requestOptions){
        Glide.with(context).load(url).apply(requestOptions).into(imageView);
    }

    public static RequestOptions getOptions(int res){
        RequestOptions options = new RequestOptions().placeholder(res).diskCacheStrategy(DiskCacheStrategy.NONE);
        return options;
    }

    private static RequestOptions getOptions(){
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.bg_header_nav).diskCacheStrategy(DiskCacheStrategy.NONE);
        return options;
    }

    private static RequestOptions getCircleOptions(){
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.bg_header_nav).transform(new CircleCrop());
        return options;
    }

    private static RequestOptions getBlurOptions(){
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.bg_header_nav).transform(new BlurTransformation());
        return options;
    }
}
