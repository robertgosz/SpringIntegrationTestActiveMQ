package com.example.springtest.test;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.util.concurrent.ListenableFuture;

@MessagingGateway
public interface MessageGateway {

    /***
     *
     *  With SpringBoot 2.1.3.RELEASE the replyTimeout is not needed here and receiveTimeout from
     *  configuration works as expected (JmsTimeoutException thrown and onFailure called)
     *
     *  With SpringBoot 2.7.8 timeout is never thrown when replyTimeout is missing here.
     *  If it's set, the behaviour depends on timeout values, but it's not as one could expect.
     *  When replyTimeout is higher than receiveTimeout exception is thrown and onFailure is called,
     *  but it's thrown after replyTimeout amount of time with message: "No reply in receiveTimeout ms"
     *  When receiveTimeout value is higher than replyTimeout value (or both are equal) exception is not
     *  being thrown, instead onSuccess is being called with null payload after replyTimeout.
     *
     *  What I also noticed - even if replyTimeout > receiveTimeout the behaviour is not consistent/repeatable.
     *  Sometimes instead of having onFailure called - onSuccess is being called anyway with null payload
     *  followed by a WARN log entry from MessagingTemplate$TemporaryReplyChannel: "Reply message received but
     *  the receiving thread has exited due to a timeout: ErrorMessage [payload=org.springframework.messaging.MessageHandlingException:
     *  nested exception is org.springframework.integration.jms.JmsTimeoutException: No reply in 'receiveTimeout' ms" (after replyTimeout ms)
     *
     **/
    @Gateway(requestChannel = "requestChannel", replyChannel = "responseChannel") //, replyTimeout = 20000
    ListenableFuture<Message<String>> sendAndReceiveMessage(Message<String> request);

}
