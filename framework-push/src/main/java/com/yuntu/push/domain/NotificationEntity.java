package com.yuntu.push.domain;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.yuntu.common.utils.JSONUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 林晓辉 on 14-5-21.
 */
public class NotificationEntity implements Serializable{

	private static final long serialVersionUID = -8844172059409168666L;
	private String title;
    private String description;
    private String sound;
    private Integer badge;
    private Map<String,String> params = new HashMap<String, String>();
    private Map<String,String> aps = new HashMap<String, String>(4);
    private boolean asynchronous = false;
    //设置默认离线时间(用户不在线消息在服务器存活多久 秒)
    private Integer timeToLive = 86400;
    
    /**可选 APNs是否生产环境  True 表示推送生产环境，False 表示要推送开发环境； 如果不指定则为推送生产环境。
	(消息) JPush 官方 API LIbrary (SDK) 默认设置为推送 “开发环境”。*/
    private boolean apnsProduction = false;

    public static NotificationEntity  newInstance(String title){
        return new NotificationEntity(title);
    }

    public static NotificationEntity  newInstance(String title,String description){
        return new NotificationEntity(title,description);
    }
    public static NotificationEntity  newInstance(String title,String description,Map<String,String> ext){
        return new NotificationEntity(title,ext,description);
    }

    private NotificationEntity(){
    }

    private NotificationEntity(String title){
        setTitle(title);
        setDescription(title);
    }

    private NotificationEntity(String title,String description){
        setDescription(description);
        setTitle(title);
    }

    public NotificationEntity(String title, Map<String, String> params, String description) {
        this.title = title;
        this.params = params;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public NotificationEntity setTitle(String title) {
        aps.put("alert",title);
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public String getSound() {
        return sound;
    }

    public NotificationEntity setSound(String sound) {
        aps.put("sound",sound);
        this.sound = sound;
        return this;
    }

    public Integer getBadge() {
        return badge;
    }

    public NotificationEntity setBadge(Integer badge) {
        aps.put("badge",""+badge);
        this.badge = badge;
        return this;
    }

    public NotificationEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String notificationJson(){
        Map<String,Object> json=new HashMap<String, Object>();
        json.put("title",this.getTitle());
        json.put("description",this.getDescription());
        json.put("aps",aps);
        json.putAll(this.params);
        try {
            return JSONUtils.toJSON(json);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public Map<String, String> getParams() {
        return params;
    }

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	
    public boolean isAsynchronous() {
        return asynchronous;
    }

	public NotificationEntity setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
        return this;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public NotificationEntity setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }
    
    public boolean isApnsProduction(){
    	return apnsProduction;
    }
    
    public NotificationEntity setApnsProduction(boolean apnsProduction){
    	this.apnsProduction = apnsProduction;
    	return this;
    }
}
