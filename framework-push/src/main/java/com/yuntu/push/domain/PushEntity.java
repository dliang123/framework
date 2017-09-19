package com.yuntu.push.domain;

import java.io.Serializable;

/**
 * Created by 000685 on 2014/10/22.
 */
public class PushEntity implements Serializable{

	private static final long serialVersionUID = 5606291640212419174L;
	private Object client;
    private Object message;

    public PushEntity(Object client, Object message) {
        this.client = client;
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getClient() {
        return client;
    }

    public void setClient(Object client) {
        this.client = client;
    }
}
