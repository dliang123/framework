package com.yuntu.page;

import java.io.Serializable;

/**
 *  16/1/4.
 */
public class PageRequestCustom implements Serializable{


    private int pageIndex=1;

    private int pageSize=10;

    private Object ext;

    public PageRequestCustom(){

    }

    public PageRequestCustom(int pageIndex, int pageSize){
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int offset(){
       return  Math.max((pageIndex-1),0) * pageSize;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }
}
