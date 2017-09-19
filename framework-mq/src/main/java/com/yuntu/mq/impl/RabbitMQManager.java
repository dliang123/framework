/**  **/
package com.yuntu.mq.impl;

import java.net.URI;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.yuntu.mq.ExchangeType;

/**
 * @author
 * @date 2016年3月25日
 */
@Component
public class RabbitMQManager {

	private Builder builder;

	private RabbitAdmin admin;

	public RabbitMQManager() {
		this.builder = this.new Builder();
	}

	public Builder getBuild() {
		return builder;
	}

	public class Builder {

		private Queue queue;
		
		private Exchange exchange;
		
		public Builder() {
		}

		public Builder connection(String uri) {
			URI uriObj = URI.create(uri);
			ConnectionFactory cf = new CachingConnectionFactory(uriObj);
			admin = new RabbitAdmin(cf);
			return this;
		}

		public Builder queue(String queueName) {
			checkRabbitAdmin();
			queue = new Queue(queueName, true, false, false);
			admin.declareQueue(queue);
			return this;
		}

		public Builder exchange(ExchangeType type, String name) {
			checkRabbitAdmin();
			exchange = getExchange(type, name);
			admin.declareExchange(exchange);
			return this;
		}

		public Builder binding(String routingKey) {
			checkRabbitAdmin();
			Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
			admin.declareBinding(binding);
			return this;
		}
		
		public RabbitMQTemplate build() {
			checkRabbitAdmin();
			RabbitTemplate template = admin.getRabbitTemplate();
			return new RabbitMQTemplate(template);
		}

		private void checkRabbitAdmin() {
			if (admin == null) {
				throw new NullPointerException("请先调用connection方法进行构造");
			}
		}

		private Exchange getExchange(ExchangeType type, String name) {
			switch (type) {
			case direct:
				return new DirectExchange(name, true, false);
			case topic:
				return new TopicExchange(name, true, false);
			case fanout:
				return new FanoutExchange(name, true, false);
			default:
				return null;
			}
		}
	}

}
