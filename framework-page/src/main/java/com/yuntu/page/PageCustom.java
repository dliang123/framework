package com.yuntu.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 *  16/1/4.
 */

public class PageCustom<T> implements Serializable{

    /** 总记录数 **/
    private long totalElements;
    /** 总页数 **/
    private long totalPages;
    /** 当前页码 **/
    private int number;

    /** 每页大小 **/
    private int size;

    /** 数据 **/
    private List<T> content;

    private Object ext;

    private PageCustom(){

    }


    public PageCustom(long totalElements,PageRequestCustom request, List<T> content){

        this(totalElements,request.getPageIndex(),request.getPageSize(),content);
    }

    private PageCustom(long totalElements, int number, int size, List<T> content){

        this.totalElements = totalElements;
        this.number = number;
        this.size = size;
        this.content = content;
        if( null == this.content ){
            this.content = Collections.EMPTY_LIST;
        }

        this.totalPages = ( totalElements + size-1 )/size;
    }


    public int getNumberOfElements(){
        return content.size();
    }


    /** 是否首页 **/
    public boolean isFirst(){
        return this.number == 1;
    }

    /** 是否最后一页 **/
    public boolean isLast(){
        return this.number == this.totalPages;
    }

    /** 是否有下一页 **/
    public boolean isHasNext(){
        return this.number < this.totalPages;
    }


    /** 是否有上一页 **/
    public boolean isHasPrevious(){
        return this.number>1;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }
}
