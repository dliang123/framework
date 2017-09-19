package com.yuntu.jpa.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuntu.base.Status;
import com.yuntu.jpa.AuditEntity;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Objects;

/***
 * 状态变更事件实体
 * @param <USER>    用户类型
 * @param <STATUS>  状态
 */
@MappedSuperclass
public abstract class StatusAuditEntity<USER extends Serializable, STATUS extends Status> extends AuditEntity<USER>
        implements StatusChange<STATUS> {

    @JsonIgnore
    @Transient
    /** 当前状态 **/
    public abstract STATUS currentStatus();

    @JsonIgnore
    @Transient
    public boolean isStatusChange(){
        return !Objects.equals(currentStatus(),snapshotStatus);
    }

    @JsonIgnore
    @Transient
    STATUS snapshotStatus;

    @PostLoad
    void snapshot(){
        snapshotStatus = currentStatus();
    }
}