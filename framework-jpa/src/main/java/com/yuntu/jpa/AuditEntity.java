package com.yuntu.jpa;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/***
 * 审计实体
 * @param <USER>    用户类型( 创建人,修改人类型 )
 */
@SuppressWarnings("serial")
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditEntity<USER extends Serializable> implements Serializable{

    @CreatedBy
    @Column(name = "create_by", updatable = false)
    protected USER createBy;
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    protected Date createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    protected USER lastModifiedBy;
    @LastModifiedDate
    @Column(name = "last_modified_date")
    protected Date lastModifiedDate;

    @Version
    protected Long version;

    public USER getCreateBy() {
        return createBy;
    }

    public void setCreateBy(USER createBy) {
        this.createBy = createBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public USER getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(USER lastModifiedBy) {
        lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
