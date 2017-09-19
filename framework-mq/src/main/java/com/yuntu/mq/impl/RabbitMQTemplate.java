/**  **/
package com.yuntu.mq.impl;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.PendingConfirm;

import com.yuntu.mq.AMQPTemplate;

/**
 * RabbitMQ 实现
 * 
 * @author icewang
 * @date 2016年3月24日
 */
public class RabbitMQTemplate implements AMQPTemplate {
	
	private RabbitTemplate template;

	private static final String charset = "UTF-8";

	public RabbitMQTemplate() {}
	
	public RabbitMQTemplate(RabbitTemplate template) {
		this.template = template;
	}

	/*
	 * 发布消息
	 * 
	 * @see com.yuntu.mq.AMQPTemplate#publish(java.lang.String)
	 * 
	 * @date 2016年3月24日
	 */
	@Override
	public void publish(String routingKey, String message) throws Exception {
		if (message == null)
			return;
		checkInitialized();
		if(routingKey != null) {
			template.setRoutingKey(routingKey);
		}
		byte[] body = message.getBytes(charset);
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentEncoding(charset);
		Message msg = new Message(body, new MessageProperties());
		template.send(msg);
	}


	/* 发布消息
	 * @see com.yuntu.mq.AMQPTemplate#publish(java.lang.String)
	 * @date 2016年3月25日
	 */
	@Override
	public void publish(String message) throws Exception {
		publish(null, message);
	}

	/*
	 * 接收消息
	 * 
	 * @see com.yuntu.mq.AMQPTemplate#receive()
	 * 
	 * @date 2016年3月24日
	 */
	@Override
	public String receive(String queueNames) throws Exception {
		Message msg = template.receive(queueNames);
		return msg == null ? null : new String(msg.getBody(), charset);
	}

	public RabbitTemplate getTemplate() {
		return template;
	}

	public void setTemplate(RabbitTemplate template) {
		this.template = template;
	}
	
	@Override
	public void setRoutingKey(String routingKey) throws Exception {
		checkInitialized();
		template.setRoutingKey(routingKey);
	}
	
	private void checkInitialized() throws Exception {
		if (template == null) {
			throw new NullPointerException("Template is not initialized.");
		}
	}
}
