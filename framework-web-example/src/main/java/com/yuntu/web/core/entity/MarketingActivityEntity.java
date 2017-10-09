package com.yuntu.web.core.entity;

import com.yuntu.jpa.AuditEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 营销活动定义
 * Created by jackdeng on 2017/8/3.
 */
@Entity
@Table(name = "marketing_activity_define")
public class MarketingActivityEntity extends AuditEntity<Long> {

    //主键、自动生成
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",insertable = false, updatable = false)
    private Long id;
    @Column(name = "mkt_tool_id" )
    private Long mktToolId;


    //活动标题
    @Column(name = "title")
    private String title;
    //ENUM，类型（纯图、图文、纯文）
    @Column(name = "media_type",length = 20)
    @Enumerated(value=EnumType.STRING)
    private MediaType mediaType;
    //开始时间，只支持到分钟，且只支持00、30
    @Column(name = "start_time")
    private Date startTime;
    //结束时间，只支持到分钟，且只支持00、30
    @Column(name = "end_time")
    private Date endTime;
    //活动价，单位元
    @Column(name = "act_price")
    private BigDecimal actPrice;
    //单买价、销售价，单位元
    @Column(name = "sale_price")
    private BigDecimal salePrice;
    //活动备注
    @Column(name = "remark",length = 500)
    private String remark;
    //活动图片
    @Column(name = "act_image_url")
    private String actImageUrl;
    //活动详情图片
    @Column(name = "act_detail_image_url")
    private String actDetailUrl;
    //商品ID
    @Column(name = "item_id")
    private Long itemId;
    //商品名称
    @Column(name = "item_name")
    private String itemName;
    //是否已经删除
    @Column(name = "is_delete")
    private Boolean hasDelete;
    //活动标签
    @Column(name = "activity_tag")
    private String activityTag;

    public String getActivityTag() {
        return activityTag;
    }

    public void setActivityTag(String activityTag) {
        this.activityTag = activityTag;
    }

    public Boolean getHasDelete() {
        return hasDelete;
    }

    public void setHasDelete(Boolean hasDelete) {
        this.hasDelete = hasDelete;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getActDetailUrl() {
        return actDetailUrl;
    }

    public void setActDetailUrl(String actDetailUrl) {
        this.actDetailUrl = actDetailUrl;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getActPrice() {
        return actPrice;
    }

    public void setActPrice(BigDecimal actPrice) {
        this.actPrice = actPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActImageUrl() {
        return actImageUrl;
    }

    public void setActImageUrl(String actImageUrl) {
        this.actImageUrl = actImageUrl;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getMktToolId() {
        return mktToolId;
    }

    public void setMktToolId(Long mktToolId) {
        this.mktToolId = mktToolId;
    }

}
