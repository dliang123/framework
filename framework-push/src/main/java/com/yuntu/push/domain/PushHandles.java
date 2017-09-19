package com.yuntu.push.domain;


import com.google.common.collect.Lists;
import com.yuntu.push.Pushable;
import com.yuntu.push.constant.DeviceType;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 林晓辉 on 14-6-19.
 */
public class PushHandles {

    private java.util.Map<DeviceType,List<Pushable>> handles=new HashMap<DeviceType, List<Pushable>>();

    public void register(DeviceType deviceType,Class<? extends Pushable> cls){

        register(cls,deviceType);
//        if( DeviceType.all == deviceType ){ //注册所有
//            register( cls ,DeviceType.values() );
//        }else{//注册指定类型与所有
//            register( cls ,DeviceType.all,deviceType );
//        }
    }

    private void register(Class<? extends Pushable> cls,DeviceType ... types){

        for(DeviceType deviceType : types){

            if( !handles.containsKey(deviceType) ){
                handles.put(deviceType,Lists.newArrayList());
            }

            for(Pushable pushable :  handles.get(deviceType) ){
                if( pushable != null && pushable.getClass().equals( cls ) ){
                    return;
                }
            }

            handles.get(deviceType).add(newInstance(cls));
        }

    }

    public Collection<Pushable> getHandle(DeviceType deviceType){

        return handles.get(deviceType);
    }

//    public Collection<Pushable> handles(){
//        List result=Lists.newArrayList();
//        for( Collection<Pushable> pushable :handles.values() ){
//            result.add(pushable);
//        }
//        return result;
//    }

    private Pushable newInstance(Class<? extends Pushable> pushable){
        try {
            return pushable.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private PushHandles pushHandles;

        public Builder register(DeviceType deviceType,Class<? extends Pushable> pushable) {
            if( null == pushHandles  ){
                pushHandles = new PushHandles();
            }
            pushHandles.register(deviceType,pushable);
            return this;
        }

        public PushHandles build() {
            return pushHandles;
        }
    }
}
