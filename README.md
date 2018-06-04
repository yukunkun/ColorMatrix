#  ColorMatrix 的使用
##  在应用宝上可以搜索下载APP MatrixPhoto,就是基于这个 　
####  功能主要来至于 [自定义控件三部曲之绘图篇（八）——Paint之ColorMatrix与滤镜效果](http://blog.csdn.net/harvic880925/article/details/51187277)
####  这里主要使用了ColorMatrix这个类 
####  有很多简单的布局,类似与一个小的APP 　　　　　  
####  利用色彩矩阵,对bitmap的颜色加以改变,添加滤镜效果,有一些自定义view,增加效果,对图片处理,裁剪,变换.
####  自定义图库,使用contentProvide,加载本地图库　　　　 　 
####  --------------------------------------------------------------------------------------------------------
#### 有些地方使用了不同的模式写的，这也是后来接触之后才修改的
###  movie_module  　
#### 使用的retrofit MVP模式写的，也就是尝试着用一下，用的比较基础的方式
#### 这里调用的豆瓣的API，使用第三方的接口，在项目中很多地方使用了overscroll，使用弹簧效果 
#### 在详情页调用的豆瓣的网页，使用了webview、  　
####  ---------------------------------------------------------------------------------------------------------
###  weather_module　　　　
#### 这儿也是后来加上的，使用的retrofit MVP模式写的，主要是一个天气预报的模块。
#### 调用了和风天气的免费API，这里有需要的可以试着调用一下。
#### 分为今天的天气，明天的天气，和舒适度三个模块，分别调用了和风天气的不同接口来呈现的。
#### 当中接入了腾讯给广告联盟的的banner广告。 
####  ---------------------------------------------------------------------------------------------------------
### 手势密码(gesture_module)
#### 添加了手势锁,就是那种9个圆圈的手势密码,仿QQ的手势锁
#### 新增人脸识别模块,采用的是科大讯飞的人脸识别技术,对app的身份进行验证,分别使用了人脸注册和人脸检测,使用相似度来判断是否为同一个人.
####  ---------------------------------------------------------------------------------------------------------
#### 添加图灵智能机器人对话,智能聊天.　
####  ---------------------------------------------------------------------------------------------------------
#### 使用了一些简单的属性动画   动画使用的类 ValueAnimator 和 BounceInterpolator
#### 颜色的渐变使用了ArgbEvaluator 属于动画的部分，在weather_module/Animutils里有使用的部分动画，有需要的可以试用一下
####  ---------------------------------------------------------------------------------------------------------
![S70401-16001073.jpg](http://upload-images.jianshu.io/upload_images/3001453-0f19ff4218784a44.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![S70401-16020219.jpg](http://upload-images.jianshu.io/upload_images/3001453-f966e3a6c737ff50.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![S70401-16014880.jpg](http://upload-images.jianshu.io/upload_images/3001453-cdcb4993d4790e40.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![S70401-16065199.jpg](http://upload-images.jianshu.io/upload_images/3001453-8915d1fae0bd7d7c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![S70401-16073051.jpg](http://upload-images.jianshu.io/upload_images/3001453-34ab35af3d0a8b3b.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 
#### compile 'com.github.mcxtzhang:PathAnimView:V1.0.0'
#### 第三方的path动画，一个很酷炫的path动画。
####  使用的第三方主要为:       
    compile 'com.android.support:appcompat-v7:23.2.1'
    compile 'com.github.bumptech.glide:glide:3.5.2'
    compile 'com.android.support:design:23.2.1'
    compile 'com.android.support:recyclerview-v7:23.2.1'
    compile 'com.android.support:cardview-v7:23.2.1'
    compile 'org.greenrobot:eventbus:3.0.0'
    compile 'cn.jiguang.sdk:jcore:1.1.0'
    compile files('libs/GDTUnionSDK.4.9.542.min.jar')
    //bugly
    compile 'com.tencent.bugly:crashreport_upgrade:latest.release'
    compile 'com.edmodo:cropper:1.0.1'
    compile 'com.github.castorflex.smoothprogressbar:library-circular:1.0.1'
    // rxjava的相关
    compile 'io.reactivex:rxjava:1.2.1'
    compile 'io.reactivex:rxandroid:1.2.1'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.squareup.retrofit2:adapter-rxjava:2.1.0'
    //butterknife
    compile 'com.jakewharton:butterknife:8.4.0'
    apt 'com.jakewharton:butterknife-compiler:8.4.0'
    compile 'jp.wasabeef:glide-transformations:2.0.1'
    compile 'com.contrarywind:Android-PickerView:3.0.7'
    compile 'cn.qqtheme.framework:WheelPicker:1.4.3'
    compile 'me.everything:overscroll-decor-android:1.0.4'
    debugCompile 'com.squareup.leakcanary:leakcanary-android:1.5'
    compile 'com.github.mcxtzhang:PathAnimView:V1.0.0'
    compile 'de.hdodenhof:circleimageview:2.1.0'
    compile 'com.haozhang.libary:android-slanted-textview:1.2'

#### [下载](http://app.qq.com/#id=detail&appid=1105962710)
![](http://upload-images.jianshu.io/upload_images/3001453-7fc76659461b6b8e.png)

