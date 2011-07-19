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

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.integration.samples.asynchttp.model.TwitterMessage;


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
