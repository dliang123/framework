package com.yuntu.jpa.event;

/**
 * 状态变更事件
 * @param <T>
 */
public class StatusChangeEvent<T extends StatusAuditEntity>  extends ActionEvent {

    private Object snapshotStatus;
    private Object currentStatus;

    public StatusChangeEvent(OperateType operateType, T source) {
        super(operateType, source);
        this.snapshotStatus = source.snapshotStatus;
        this.currentStatus = source.currentStatus();
    }

    public Object getSnapshotStatus() {
        return snapshotStatus;
    }

    public Object getCurrentStatus() {
        return currentStatus;
    }
}