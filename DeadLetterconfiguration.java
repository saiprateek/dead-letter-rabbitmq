package com.prateek.deadletter.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DeadLetterconfiguration {

/*
Here we will implement dead letter feature for two differenr queue.
Each queue will have thre own dead letter queue and dead letter exchange.
for example for queue test1 will have dead letter queue test1-deadletter-queue
and exchange test1-deadletter-exchange.
*/
  public static final String MAIN_QUEUE = "test1-queue";
  public static final String DEADLETTER_QUEUE = "test1-deadletter-queue";
  public static final String DEADLETTER_EXCHANGE = "test1-deadletter-exchange";

  @Autowired
  private ConnectionFactory connectionFactory;
  
  @Bean
	TopicExchange deadletterExchange() {
		return new TopicExchange(DEADLETTER_EXCHANGE);
	}

  @Bean
  public Queue deadletterQueue() {
    Map<String, Object> args = new HashMap<String, Object>();
    args.put("x-dead-letter-exchange", "DEADLETTER_EXCHANGE");  // The dead letter Exchange
    args.put("x-dead-letter-routing-key", MAIN_QUEUE);  // Route to the dead letter queue when the TTL occurs
    args.put("x-message-ttl", 5000);   // TTL 5 seconds
    return new Queue(DEADLETTER_QUEUE, false, false, false, args);
  }
  
  @Bean
  public Queue mainQueue() {
    Map<String, Object> args = new HashMap<String, Object>();
    args.put("x-dead-letter-exchange", "DEADLETTER_EXCHANGE");  // dead letter Exchange
    args.put("x-dead-letter-routing-key", DEADLETTER_QUEUE);  // Route to the main queue when the TTL occurs
    args.put("x-message-ttl", 5000);   // TTL 5 seconds
    return new Queue(MAIN_QUEUE, false, false, false, args);
  }

 @Bean
	Binding mainQueueBinding(Queue mainQueue, TopicExchange deadletterExchange) {
		return BindingBuilder.bind(mainQueue).to(deadletterExchange).with(MAIN_QUEUE);
	} 
  
  @Bean
	Binding deadletterQueueBinding(Queue deadletterQueue, TopicExchange deadletterExchange) {
		return BindingBuilder.bind(deadletterQueue).to(deadletterExchange).with(DEADLETTER_QUEUE);
	} 
  
}
