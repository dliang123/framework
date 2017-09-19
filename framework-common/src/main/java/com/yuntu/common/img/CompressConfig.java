package com.yuntu.common.img;


/*
 * Copyright (C), 2012-2014, 上海好屋网信息技术有限公司
 * Author:
 * Date:     14-12-27
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            14-12-27           00000001         创建文件
 *
 */
public class CompressConfig {

    //原图
    public static final CompressConfig DEFAULT = new CompressConfig(Size.DEFAULT, Algorithm.SCALE_SMOOTH,0.75f);

    //质量,0-0.25 低质量,0.25-0.75 中等 075+ 高质量
    private float quality=0.75f;
    private Size size = Size.DEFAULT;
    private Algorithm algorithm=Algorithm.SCALE_SMOOTH;

    public CompressConfig(Size size, Algorithm algorithm, float quality) {
        this.size = size;
        this.algorithm = algorithm;
        this.quality = quality;
    }


    public Size getSize() {
        return size;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public float getQuality() {
        return quality;
    }

    public static CompressConfig newInstance(Size size){
        return newInstance(size, Algorithm.SCALE_SMOOTH);
    }

    public static CompressConfig newInstance(Size size,Algorithm algorithm){
        return newInstance(size, algorithm,0.75f);
    }

    public static CompressConfig newInstance(Size size,Algorithm algorithm,float quality){
        return new CompressConfig(size, algorithm, quality);
    }
}