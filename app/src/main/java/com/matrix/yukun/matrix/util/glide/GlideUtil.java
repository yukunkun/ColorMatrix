package com.matrix.yukun.matrix.util.glide;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.GlideCircleWithBorder;

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
    public static void loadImage(String url, ImageView imageView,RequestOptions options){
        Glide.with(context).load(url).apply(options).into(imageView);
    }
    public static void loadPlaceholderImage(String url, ImageView imageView){
        Glide.with(context).load(url).apply(getOptions()).into(imageView);
    }

    public static void loadCircleImage(String url, ImageView imageView){
        Glide.with(context).load(url).apply(getCircleOptions()).into(imageView);
    }

    public static void loadCircleImage(int res, ImageView imageView){
        Glide.with(context).load(res).apply(getCircleOptions()).into(imageView);
    }

    public static void loadCircleBoardImage(String url, ImageView imageView){
        Glide.with(context).load(url).apply(getBoardOptions()).into(imageView);
    }

    public static void loadCircleBoardImage(int res, ImageView imageView){
        Glide.with(context).load(res).apply(getBoardOptions()).into(imageView);
    }

    public static void loadBlurImage(String url, ImageView imageView){
        Glide.with(context).load(url).apply(getBlurOptions()).into(imageView);
    }

    public static void loadOptionsImage(String url, ImageView imageView,RequestOptions requestOptions){
        Glide.with(context).load(url).apply(requestOptions).into(imageView);
    }

    public static void loadImage(int res, ImageView imageView){
        Glide.with(context).load(res).into(imageView);
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
    private static RequestOptions getBoardOptions(){
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.bg_header_nav).transform(new GlideCircleWithBorder(context, 1,Color.parseColor("#b1b1b1")));
        return options;
    }

    public static RequestOptions getErrorOptions(int place,int fall,int error ){
        RequestOptions options = new RequestOptions()
//                .placeholder(place)//图片加载出来前，显示的图片
                .fallback(fall) //url为空的时候,显示的图片
                .error(error);//图片加载失败后，显示的图片
        return options;
    }

}
