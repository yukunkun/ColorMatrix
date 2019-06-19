##  多工具娱乐集合APP （应用宝已经上线 搜索MatrixPhoto即可下载）
#### 这是对matrixphoto的一个迁移，matrixphoto功能上做了很多的修改，添加了很多的工具集合，比如zxing的二维码扫面，二维码生成，添加了爱心弹幕的功能模块，备忘录记录琐事，还有原来的天气模块，GIF制作，视频图片转GIF功能，在平时的一些开发中难免也会使用到这些东西，这个也可以作为一些迁移使用。首页调用了一些开源的API。包括和风天气，聚合数据等，使它形成了一个完整的APP功能。一键卡片分享功能，提高分享的美观性。设计了黑夜模式的切换，使用也比较简单可以查看
[超简单的 Android夜晚模式实现](https://www.jianshu.com/p/f1c09e483b11)  这儿有实现

#### 部分页面截图
![S81212-10302629.jpg](https://upload-images.jianshu.io/upload_images/3001453-eb8cfbc5dce17a40.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10310255.jpg](https://upload-images.jianshu.io/upload_images/3001453-d13534b2bb20deba.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10310666.jpg](https://upload-images.jianshu.io/upload_images/3001453-50fea1848e9c0687.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10311954.jpg](https://upload-images.jianshu.io/upload_images/3001453-29aeb4788f1c52d8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10313965.jpg](https://upload-images.jianshu.io/upload_images/3001453-cd0f07a36a58df45.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10314652.jpg](https://upload-images.jianshu.io/upload_images/3001453-54c150f7726d6a8e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-11061530.jpg](https://upload-images.jianshu.io/upload_images/3001453-d87fa8014a5cf180.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10320229.jpg](https://upload-images.jianshu.io/upload_images/3001453-a4a313b02fa74e4d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10320748.jpg](https://upload-images.jianshu.io/upload_images/3001453-59015ccbe1fb5ac8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10334831.jpg](https://upload-images.jianshu.io/upload_images/3001453-d00ec5ef7851b978.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10340634.jpg](https://upload-images.jianshu.io/upload_images/3001453-dd13e54f59b4952f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10343856.jpg](https://upload-images.jianshu.io/upload_images/3001453-13dcbb05cd6f3757.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10353286.jpg](https://upload-images.jianshu.io/upload_images/3001453-921d879f3807c6cc.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10354562.jpg](https://upload-images.jianshu.io/upload_images/3001453-91370523df922534.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10360296.jpg](https://upload-images.jianshu.io/upload_images/3001453-ae6eda9364975a7b.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10363071.jpg](https://upload-images.jianshu.io/upload_images/3001453-3c6a900f9f86a909.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10363600.jpg](https://upload-images.jianshu.io/upload_images/3001453-8325de14896eac48.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10363948.jpg](https://upload-images.jianshu.io/upload_images/3001453-69e95130ed77b70f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10365742.jpg](https://upload-images.jianshu.io/upload_images/3001453-8f4ff112cddc8a9c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![S81212-10370087.jpg](https://upload-images.jianshu.io/upload_images/3001453-92c239133bab99bc.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 项目中也使用了很多第三方 
        compile 'com.github.bumptech.glide:glide:3.5.2'
        compile 'com.android.support:design:26.1.0'
        compile 'com.android.support:recyclerview-v7:26.1.0'
        compile 'com.android.support:cardview-v7:26.1.0'
        compile 'org.greenrobot:eventbus:3.0.0'
        compile 'com.tencent.bugly:crashreport_upgrade:latest.release'
        compile 'com.edmodo:cropper:1.0.1'
        compile 'com.github.castorflex.smoothprogressbar:library-circular:1.0.1'
        compile 'io.reactivex:rxjava:1.2.1'
        compile 'io.reactivex:rxandroid:1.2.1'
        compile 'com.squareup.retrofit2:retrofit:2.3.0'
        compile 'com.squareup.retrofit2:converter-gson:2.3.0'
        compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
        compile 'com.jakewharton:butterknife:8.4.0'
        annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'
        compile 'jp.wasabeef:glide-transformations:2.0.1'
        compile 'me.everything:overscroll-decor-android:1.0.4'
        compile 'com.github.mcxtzhang:PathAnimView:V1.0.0'
        compile 'com.h6ah4i.android.widget.verticalseekbar:verticalseekbar:0.7.0'
        compile 'de.hdodenhof:circleimageview:2.1.0'
        compile 'com.haozhang.libary:android-slanted-textview:1.2'
        compile 'com.getbase:floatingactionbutton:1.10.1'
        compile 'com.github.chrisbanes.photoview:library:1.2.4'
        compile 'com.isanwenyu.highlight:highlight:1.8.0'
        compile 'org.litepal.android:core:1.6.0'
        compile 'com.zhy:okhttputils:2.6.2'
        compile 'com.google.code.gson:gson:2.8.2'
        compile 'com.bigkoo:convenientbanner:2.0.5'
        compile 'com.scwang.smartrefresh:SmartRefreshHeader:1.0.4-6'
        compile 'com.google.zxing:core:3.3.0'
        compile 'com.jrummyapps:colorpicker:2.1.6'
        compile 'com.wang.avi:library:2.1.3'
        compile 'joda-time:joda-time:2.9.4'
        compile 'com.contrarywind:Android-PickerView:4.1.6'
        implementation project(':pluglin_special')
        implementation files('libs/android-query-full.0.26.7.jar')
        implementation files('libs/ZBarDecoder.jar')

#### [下载](http://app.qq.com/#id=detail&appid=1105962710)
![应用宝下载地址](http://upload-images.jianshu.io/upload_images/3001453-7fc76659461b6b8e.png)
