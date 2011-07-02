package com.hillert.websockets.integration;

import org.springframework.integration.twitter.core.Tweet;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.hillert.websockets.model.TwitterMessage;

public class TweetTransformer {

    public TwitterMessage transform(Tweet tweet) {

        final TwitterMessage twitterMessage = new TwitterMessage(
                tweet.getId(),
                tweet.getCreatedAt(),
                tweet.getText(),
                tweet.getFromUser(),
                tweet.getProfileImageUrl());

        return twitterMessage;
    }

}
