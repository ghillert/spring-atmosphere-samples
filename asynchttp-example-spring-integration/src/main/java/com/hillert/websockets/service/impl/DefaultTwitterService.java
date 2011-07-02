/*
 * Copyright 2002-2010 the original author or authors
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.hillert.websockets.service.impl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.hillert.websockets.model.TwitterMessage;
import com.hillert.websockets.service.TwitterService;

/**
 * Implementation of the TwitterService interface.
 */
@Service
public class DefaultTwitterService implements TwitterService {

    /** Holds a collection of polled Twitter messages */
    private Map<Long, TwitterMessage> twitterMessages;

    @Autowired
    @Qualifier("controlBusChannel")
    private DirectChannel channel;

    /**
     * Constructor that initializes the 'twitterMessages' Map as a simple LRU
     * cache. @See http://blogs.oracle.com/swinger/entry/collections_trick_i_lru_cache
     */
    public DefaultTwitterService() {

        twitterMessages = new LinkedHashMap<Long, TwitterMessage>( 10, 0.75f, true ) {

            private static final long serialVersionUID = 1L;

            protected boolean removeEldestEntry( java.util.Map.Entry<Long, TwitterMessage> entry ) {
                return size() > 10;
            }

        };

    }

    /** {@inheritDoc} */
    @Override
    public Collection<TwitterMessage> getTwitterMessages() {
        return twitterMessages.values();
    }

    /** {@inheritDoc} */
    @Override
    public void startTwitterAdapter() {

        final MessagingTemplate m = new MessagingTemplate();
        final Message<String> operation = MessageBuilder.withPayload("@twitter.start()").build();

        m.send(channel, operation);

    }

    /** {@inheritDoc} */
    @Override
    public void stopTwitterAdapter() {

        final MessagingTemplate m = new MessagingTemplate();
        final Message<String> operation = MessageBuilder.withPayload("@twitter.stop()").build();

        m.send(channel, operation);

    }

}
