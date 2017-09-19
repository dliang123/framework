package com.yuntu.web.listener;

import org.apache.commons.fileupload.ProgressListener;

/*
 * Author:   林晓辉
 * Date:     15-1-22
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           15-1-22           00000001         创建文件
 *
 */
public class FileUploadListener implements ProgressListener {

    private boolean complete=false;
    private long bytesRead;
    private long contentLength;
    private int items;

    public void reset(){
        complete=false;
        this.bytesRead = 0;
        this.contentLength = 0;
        this.items = 0;
    }

    public void complete(){
        complete=true;
    }

    public boolean isComplete(){
        return complete;
    }

    public void update(long pBytesRead, long pContentLength, int pItems) {
        this.bytesRead = pBytesRead;
        this.contentLength = pContentLength;
        this.items = pItems;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public int getItems() {
        return items;
    }
}
