package com.example.springtest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.jms.JmsOutboundGateway;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ConfigurationProperties(prefix = "gateway")
public class GatewayConfig {

    protected int receiveTimeout;
    protected int replyConcurrentConsumers;
    protected String requestDestination;
    protected String responseDestination;
    protected int replyExecutorCorePoolSize;
    protected int reqExecutorPoolSize;

    @Bean
    public MessageChannel requestChannel(AsyncTaskExecutor reqExecutor) {
        return new ExecutorChannel(reqExecutor);
    }

    @Bean
    public MessageChannel responseChannel() {
        return new QueueChannel();
    }

    @Bean
    public JmsOutboundGateway.ReplyContainerProperties replyContainerProperties(AsyncTaskExecutor replyExecutor) {
        JmsOutboundGateway.ReplyContainerProperties properties = new JmsOutboundGateway.ReplyContainerProperties();
        properties.setTaskExecutor(replyExecutor);
        properties.setConcurrentConsumers(replyConcurrentConsumers);
        return properties;
    }

    @Bean
    @ServiceActivator(inputChannel = "requestChannel")
    public JmsOutboundGateway jmsOutboundGateway(MessageChannel responseChannel,
                                                 CachingConnectionFactory activeMQCachingConnectionFactory,
                                                 JmsOutboundGateway.ReplyContainerProperties replyContainerProperties) {

        JmsOutboundGateway gateway = new JmsOutboundGateway();
        gateway.setConnectionFactory(activeMQCachingConnectionFactory);
        gateway.setRequestDestinationName(requestDestination);
        gateway.setReplyDestinationName(responseDestination);
        gateway.setReplyChannel(responseChannel);
        gateway.setCorrelationKey("customCorrelationId");
        gateway.setAsync(true);
        gateway.setRequiresReply(true);
        gateway.setUseReplyContainer(true);
        gateway.setReceiveTimeout(receiveTimeout);
        gateway.setDeliveryPersistent(false);
        gateway.setExplicitQosEnabled(true);
        gateway.setReplyContainerProperties(replyContainerProperties);
        return gateway;
    }

    protected ThreadPoolTaskExecutor getThreadPoolTaskExecutor(int poolSize) {
        ThreadPoolTaskExecutor wm = new ThreadPoolTaskExecutor();
        wm.setCorePoolSize(poolSize);
        wm.setMaxPoolSize(poolSize);
        wm.initialize();
        return wm;
    }

    @Bean
    AsyncTaskExecutor reqExecutor() {
        return getThreadPoolTaskExecutor(reqExecutorPoolSize);
    }

    @Bean
    AsyncTaskExecutor replyExecutor() {
        return getThreadPoolTaskExecutor(replyExecutorCorePoolSize);
    }

    public int getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(int receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public int getReplyConcurrentConsumers() {
        return replyConcurrentConsumers;
    }

    public void setReplyConcurrentConsumers(int replyConcurrentConsumers) {
        this.replyConcurrentConsumers = replyConcurrentConsumers;
    }

    public String getRequestDestination() {
        return requestDestination;
    }

    public void setRequestDestination(String requestDestination) {
        this.requestDestination = requestDestination;
    }

    public String getResponseDestination() {
        return responseDestination;
    }

    public void setResponseDestination(String responseDestination) {
        this.responseDestination = responseDestination;
    }

    public int getReplyExecutorCorePoolSize() {
        return replyExecutorCorePoolSize;
    }

    public void setReplyExecutorCorePoolSize(int replyExecutorCorePoolSize) {
        this.replyExecutorCorePoolSize = replyExecutorCorePoolSize;
    }

    public int getReqExecutorPoolSize() {
        return reqExecutorPoolSize;
    }

    public void setReqExecutorPoolSize(int reqExecutorPoolSize) {
        this.reqExecutorPoolSize = reqExecutorPoolSize;
    }

}
