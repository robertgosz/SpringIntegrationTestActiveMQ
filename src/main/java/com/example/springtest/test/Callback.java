package com.example.springtest.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class Callback implements ListenableFutureCallback<Message<String>> {

    private static final Logger LOG = LoggerFactory.getLogger(Callback.class);

    @Override
    public void onFailure(@NonNull Throwable throwable) {

        LOG.error("______ Failure ______: {}", throwable.toString());

    }

    @Override
    public void onSuccess(Message result) {

        LOG.info("______ Success ______: {}", result);

    }

}
