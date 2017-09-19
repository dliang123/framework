package com.yuntu.common.img;

/**
 * 图片压缩算法
 */
public enum Algorithm {
    //默认的图像缩放算法   1
    SCALE_DEFAULT,
    //快速压缩算法         2
    SCALE_FAST,
    //高质量压缩          4
    SCALE_SMOOTH,
    //复制                8
    SCALE_REPLICATE,
    //区域平均算法        16
    SCALE_AREA_AVERAGING;

    public int value(){
        return 1 << this.ordinal();
    }
}