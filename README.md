#  ColorMatrix 的使用--
##  在应用宝上可以搜索下载APP MatrixPhoto,就是基于这个编码的
####  功能主要来至于 [自定义控件三部曲之绘图篇（八）——Paint之ColorMatrix与滤镜效果](http://blog.csdn.net/harvic880925/article/details/51187277)
####  这里主要使用了ColorMatrix这个类
####  有很多简单的布局,类似与一个小的APP
####  利用色彩矩阵,对bitmap的颜色加以改变,添加滤镜效果,有一些自定义view,增加效果,对图片处理,裁剪,变换.
####  自定义图库,使用contentProvide,加载本地图库
####  使用的第三方主要为:
####  compile 'com.github.bumptech.glide:glide:3.5.2'
####  compile 'com.android.support:recyclerview-v7:23.2.1'
####  compile 'org.greenrobot:eventbus:3.0.0'
####  compile 'cn.jiguang.sdk:jcore:1.1.0'
####  compile 'com.github.castorflex.smoothprogressbar:library-circular:1.0.1'
####
#### 使用了一些简单的属性动画   动画使用的类 ValueAnimator 和 BounceInterpolator


