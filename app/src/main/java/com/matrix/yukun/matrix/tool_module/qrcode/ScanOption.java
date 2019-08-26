package com.matrix.yukun.matrix.tool_module.qrcode;

import java.io.Serializable;

/**
 * 设置扫码页面的类
 *
 * @autor wwl
 * @date 2017-01-11 17:32
 */

public class ScanOption implements Serializable {
    public int maskColor;//遮罩层颜色
    public int cornerColor;//四个角的颜色
    public int pointColor;//中间点的颜色
    public int lineColor;//中间线的颜色
    public int lineDrawable;//中间的图片
}
