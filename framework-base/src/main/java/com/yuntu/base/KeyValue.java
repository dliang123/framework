package com.yuntu.base;

import java.io.Serializable;

public class KeyValue<K,V> implements Serializable {

	private static final long serialVersionUID = -3867010963100437598L;

	private K key;

    private V value;

    public KeyValue(){

    }
    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public KeyValue setKey(K key) {
        this.key = key;
        return this;
    }

    public V getValue() {
        return value;
    }

    public KeyValue setValue(V value) {
        this.value = value;
        return this;
    }
}
