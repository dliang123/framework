package com.yuntu.web.example.controller;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by linxiaohui on 15/11/5.
 */

@SuppressWarnings("serial")
public class LogEntity implements Serializable{


    private Date createTime;

    private String requestId;

    //log生成时间（用户请求时间），格式： yyyy-mm-dd hh:mm:ss.SSS
//    @JsonDeserialize(using=DateDeserialize.class)
    private Date requestTime;

    //该条日志隶属的session的id
    private String sessionId;

    //该条日志隶属的session的开始时间，格式：yyyy-mm-dd hh:mm:ss.SSS
    private Date sessionStartTime;


    private String host;

    private String path;

    private String parameters;


    private String pageName;

    //页面id
    private String pageId;

    //页面所属城市
    private String pageCityId;

    //订单 id
    private String  orderId;

    //产品 id
    private String productId;

    //页面搜索词
    private String pageSearchWord;

    //频道
    private String channel;

    //一级行政区或商区ID
    private String regionId;

    //二级行政区或商区ID
    private String subRegionId;


    //用户来源
    private String userSource;

    //访客id
    private String guid;

    private String userIp;

    //机器唯一标识符
    private String deviceId;

    //访客所在省
    private String provinceId;

    //ip定位城市
    private String cityId;

    //访客所在区/县
    private String districtId;

    //访客浏览器
    private String browser;

    //访客操作系统
    private String os;

    //访客使用的语言环境
    private String language;

    //访客屏幕分辨率
    private String screen;

    //访客显示屏色深
    private String color;

    //访客falsh版本
    private String flash;

    private String userAgent;

    //app版本号
    private String appVersion;

    //手机类型
    private String phoneModel;

    //gps定位城市id
    private String locateCityId;

    //经度
    private Double lat;

    //纬度
    private Double lng;

    //事件动作
    private String eventAction;

    //事件类型
    private String eventCategory;

    //事件标签
    private String eventLabel;

    //事件值
    private String eventValue;

    //来源页面
    private String referPageName;

    //来源页面id
    private String referPageId;

    //用户上一个请求的唯一id
    private String referRequestId;

    //pc ,h5 ,app ,weixin
    private String deviceType;

    //微信 id
    private String openId;

    //地图 api 类型
    private String gpsApiType;

    //业务类型
    private String business;


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(Date sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageCityId() {
        return pageCityId;
    }

    public void setPageCityId(String pageCityId) {
        this.pageCityId = pageCityId;
    }



    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPageSearchWord() {
        return pageSearchWord;
    }

    public void setPageSearchWord(String pageSearchWord) {
        this.pageSearchWord = pageSearchWord;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getSubRegionId() {
        return subRegionId;
    }

    public void setSubRegionId(String subRegionId) {
        this.subRegionId = subRegionId;
    }

    public String getUserSource() {
        return userSource;
    }

    public void setUserSource(String userSource) {
        this.userSource = userSource;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFlash() {
        return flash;
    }

    public void setFlash(String flash) {
        this.flash = flash;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getLocateCityId() {
        return locateCityId;
    }

    public void setLocateCityId(String locateCityId) {
        this.locateCityId = locateCityId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getEventAction() {
        return eventAction;
    }

    public void setEventAction(String eventAction) {
        this.eventAction = eventAction;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventLabel() {
        return eventLabel;
    }

    public void setEventLabel(String eventLabel) {
        this.eventLabel = eventLabel;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public String getReferPageName() {
        return referPageName;
    }

    public void setReferPageName(String referPageName) {
        this.referPageName = referPageName;
    }

    public String getReferPageId() {
        return referPageId;
    }

    public void setReferPageId(String referPageId) {
        this.referPageId = referPageId;
    }

    public String getReferRequestId() {
        return referRequestId;
    }

    public void setReferRequestId(String referRequestId) {
        this.referRequestId = referRequestId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getGpsApiType() {
        return gpsApiType;
    }

    public void setGpsApiType(String gpsApiType) {
        this.gpsApiType = gpsApiType;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
