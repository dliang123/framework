/**  **/
package com.yuntu.mq;

/**
 * @author
 * @date 2016年3月24日
 */
public interface AMQPTemplate {

	public void publish(String message) throws Exception;
	
	public void publish(String routingKey, String message) throws Exception;
	
	public String receive(String queueName) throws Exception;
	
	public void setRoutingKey(String routingKey) throws Exception;
}
