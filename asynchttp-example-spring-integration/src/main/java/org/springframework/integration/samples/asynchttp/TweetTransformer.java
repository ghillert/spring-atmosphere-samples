/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.samples.asynchttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.asynchttp.AsyncHttpApplicationEvent;
import org.springframework.integration.samples.asynchttp.model.StatisticMessage;
import org.springframework.integration.samples.asynchttp.model.TwitterMessage;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.twitter.core.Tweet;


public class TweetTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TweetTransformer.class);
	
    public TwitterMessage transform(Tweet tweet) {

        final TwitterMessage twitterMessage = new TwitterMessage(
                tweet.getId(),
                tweet.getCreatedAt(),
                tweet.getText(),
                tweet.getFromUser(),
                tweet.getProfileImageUrl());
        
        return twitterMessage;
    }

    /**
     * 
     * @param tweet
     * @return
     */
    public Message<TwitterMessage> prepareInternalTwitterMessage(Message<TwitterMessage> sourceMessage) {

    	final TwitterMessage sourceTwitterMessage = sourceMessage.getPayload();
    	
        final TwitterMessage destinationTwitterMessage = new TwitterMessage(
        		sourceTwitterMessage.getId(),
        		sourceTwitterMessage.getCreatedAt(),
        		sourceTwitterMessage.getText(),
        		sourceTwitterMessage.getFromUser(),
        		"images/springsource-weblogo_bigger.jpg");

        final Message<TwitterMessage> destinationMessage = MessageBuilder.withPayload(destinationTwitterMessage)
            .copyHeaders(sourceMessage.getHeaders())
            .build();
        
        return destinationMessage;
    }
    
    public Message<StatisticMessage> transformEvent(Message<AsyncHttpApplicationEvent> sourceMessage) {

    	final AsyncHttpApplicationEvent event = sourceMessage.getPayload();
    	
        final StatisticMessage destinationTwitterMessage = new StatisticMessage();
        destinationTwitterMessage.setNumberOfSubscribers(event.getBroadcastMetrics().getNumberOfSubscribers());
        destinationTwitterMessage.setSubscriberIds(event.getBroadcastMetrics().getSubscriberIds());
        
        final Message<StatisticMessage> destinationMessage = MessageBuilder.withPayload(destinationTwitterMessage)
            .copyHeaders(sourceMessage.getHeaders())
            .build();
        
        return destinationMessage;
    }
    
}
