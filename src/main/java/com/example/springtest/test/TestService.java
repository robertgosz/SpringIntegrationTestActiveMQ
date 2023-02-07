package com.example.springtest.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import java.util.HashMap;

@Service
public class TestService {

    private static final Logger LOG = LoggerFactory.getLogger(TestService.class);

    private final MessageGateway messageGateway;

    @Autowired
    public TestService(MessageGateway gateway) {
        this.messageGateway = gateway;
    }

    // Send message after context initialization - for testing
    @EventListener
    public void sendAndReceive(ContextRefreshedEvent event) {
        ListenableFuture<Message<String>> result;
        Callback callback = new Callback();
        result = messageGateway.sendAndReceiveMessage(buildMessage());
        result.addCallback(callback);
        LOG.info("______ Message sent ______");
    }

    private Message<String> buildMessage() {

        HashMap<String, Object> headersMap = new HashMap<>();

        return new Message<String>() {
            @Override
            @NonNull
            public String getPayload() {
                return "Hello There";
            }

            @Override
            @NonNull
            public MessageHeaders getHeaders() {
                return new MessageHeaders(headersMap);
            }
        };
    }

}
