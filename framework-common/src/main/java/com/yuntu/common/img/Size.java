package com.yuntu.common.img;

/*
 * Copyright (C), 2012-2014,
 * Author:
 * Date:     14-12-27
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            14-12-27           00000001         创建文件
 *
 */
public abstract class Size {

    public static final Size DEFAULT=newZoomScale(1);

    public abstract int getWidth(int oldWidth,int oldHeight);
    public abstract int getHeight(int oldWidth,int oldHeight);
    /**
     *
     * @param height 固定宽度       ( 小于1 使用默认的宽度)
     * @param ratio  比例=长度/宽度 ( 小于1 使用默认的默认比例)
     */
    public static Size newFixedHeightScale(final int height,final float ratio){

        return new Size(){
            int _height=height;
            float _ratio = ratio;

            @Override
            public int getWidth(int oldWidth, int oldHeight) {
                return (int)(getHeight(oldWidth,oldHeight) * ( _ratio > 0 ? _ratio : oldWidth/Float.valueOf(oldHeight) ) );
            }

            @Override
            public int getHeight(int oldWidth, int oldHeight) {
                return _height>0 ? _height : oldHeight;
            }
        };
    }

    /**
     *
     * @param width 固定宽度       ( 小于1 使用默认的宽度)
     * @param ratio  比例=长度/宽度 ( 小于1 使用默认的默认比例)
     */
    public static Size newFixedWidthScale(final int width,final float ratio){

        return new Size(){
            int _width=width;
            float _ratio = ratio;

            @Override
            public int getWidth(int oldWidth, int oldHeight) {
                return _width>0 ? _width : oldHeight;
            }

            @Override
            public int getHeight(int oldWidth, int oldHeight) {
                return (int)(getWidth(oldWidth, oldHeight) * ( _ratio > 0 ? _ratio : oldWidth/Float.valueOf(oldHeight) ) );
            }
        };
    }

    public static Size newZoomScale(final float ratio){

        return new Size() {
            float _ratio = ratio> 0 ?ratio:1;
            @Override
            public int getWidth(int oldWidth, int oldHeight) {
                return (int)(oldWidth * _ratio);
            }

            @Override
            public int getHeight(int oldWidth, int oldHeight) {
                return (int)(oldHeight * _ratio);
            }
        };
    }

    public static Size newFixedScale(final int width,final int height){

        return new Size() {
            private int _width=width;
            private int _height=height;
            @Override
            public int getWidth(int oldWidth, int oldHeight) {
                return _width;
            }

            @Override
            public int getHeight(int oldWidth, int oldHeight) {
                return _height;
            }
        };
    }
}
