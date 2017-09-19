package com.yuntu.base.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class StoreChain implements Store{

    private List<Store> stores;

    public StoreChain(){
        this.stores = new ArrayList<Store>();
    }

    public void addStore(Store store){
        stores.add(store);
        //排序
    }

    public String[] keys(String domainKey) {

        for(Store store:stores){
           try {
               String [] keys=store.keys(domainKey);
               if( keys != null ){
                   return keys;
               }
           }catch (Exception e){
               e.printStackTrace();
           }
        }
        return null;

    }

    public Object get(String key) {
        for(Store store:stores){
            try {
                Object v=store.get(key);
                if( v != null ){
                    return v;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public void set(String key, Object value) {
        for(Store store:stores){
            try {
                store.set(key, value);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void delete(String key) {
        for(Store store:stores){
            try {
                store.delete(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void put(String domainKey, String key, Object o) {
        for(Store store:stores){
            try {
                store.put(domainKey, key, o);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void delete(String domainKey, String key) {
        for(Store store:stores){
            try {
                store.delete(domainKey, key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void invalidate(String domainKey) {
        for(Store store:stores){
            try {
                store.invalidate(domainKey);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Object get(String domainKey, String key) {
        for(Store store:stores){
            try {
                Object v=store.get(domainKey,key);
                if( v != null ){
                    return v;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> T get(String domainKey, String key, T defaultValue) {
        for(Store store:stores){
            try {
                T v=store.get(domainKey,key,defaultValue);
                if( v != null ){
                    return v;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public Map<String, Object> entries(String domainKey) {
        for(Store store:stores){
            try {
                Map<String, Object> v=store.entries(domainKey);
                if( v != null ){
                    return v;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public void expire(String domainKey, long timeOut, TimeUnit timeUnit) {
        for(Store store:stores){
            try {
                store.expire(domainKey,timeOut,timeUnit);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean hasKey(String domainKey) {
        for(Store store:stores){
            try {
                Boolean b= store.hasKey(domainKey);
                if( b==true )
                    return b;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public int order() {
        return 0;
    }

}
