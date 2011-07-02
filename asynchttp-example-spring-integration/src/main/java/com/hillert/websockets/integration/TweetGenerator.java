package com.hillert.websockets.integration;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.hillert.websockets.model.TwitterMessage;

public class TweetGenerator {

    AtomicLong counter = new AtomicLong();

    TwitterMessage getTwitterMessage() {
        final TwitterMessage twitterMessage = new TwitterMessage(
                counter.addAndGet(1),
                new Date(),
                "Message: " + counter.get(), "generator", "http://a1.twimg.com/profile_images/93471463/gh_90px_x_90px.jpg");
        return twitterMessage;
    }



}
