package com.yuntu.jpa.event;

/**
 * 状态变更事件侦听
 * @param <T>
 */
public abstract class StatusChangeListener<T extends StatusAuditEntity> implements ActionListener<T> {

    public void handle(ActionEvent<T> event) {
        if( isHandle(event) ){
            handleChange( new StatusChangeEvent(event.getOperateType(),event.getSource()) );
        }
    }

    protected abstract void handleChange(StatusChangeEvent<T> event);

    public boolean isHandle(ActionEvent<T> event){
        if( event.getSource() instanceof StatusAuditEntity ){
            return event.getSource().isStatusChange();
        }
        return false;
    }
}