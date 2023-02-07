package com.example.springtest.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration
@ConfigurationProperties(prefix = "spring.activemq")
public class MqConfig {

    protected int sessionCacheSize;
    protected String brokerUrl;

    @Bean
    public CachingConnectionFactory activeMQCachingConnectionFactory(ActiveMQConnectionFactory activeMQConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
        cachingConnectionFactory.setReconnectOnException(true);
        cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);
        return cachingConnectionFactory;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setUseAsyncSend(true);
        factory.setBrokerURL(brokerUrl);
        return factory;
    }

    public int getSessionCacheSize() {
        return sessionCacheSize;
    }

    public void setSessionCacheSize(int sessionCacheSize) {
        this.sessionCacheSize = sessionCacheSize;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }
}
