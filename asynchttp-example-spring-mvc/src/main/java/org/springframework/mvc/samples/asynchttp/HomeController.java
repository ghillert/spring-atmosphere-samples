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
package org.springframework.mvc.samples.asynchttp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mvc.samples.asynchttp.model.TwitterMessage;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Main controller. 
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home() {
        return "home";
    }

    /**
     * Responsible for suspending the {@link HttpServletResponse} and executing 
     * a broadcasts periodically.
     */
    @RequestMapping(value="/websockets", method=RequestMethod.GET)
    @ResponseBody
    public void websockets(final AtmosphereResource<HttpServletRequest,HttpServletResponse> event) {

        final ObjectMapper mapper = new ObjectMapper();

        event.suspend();

        final Broadcaster bc = event.getBroadcaster();

        bc.scheduleFixedBroadcast(new Callable<String>() {

            private long sinceId = 0;

            @Override
            public String call() throws Exception {

                final TwitterTemplate twitterTemplate = new TwitterTemplate();
                final String searchPhrase = "springintegration OR html5 OR vfabric OR #springframework OR springsource OR #java OR #eip OR infoq OR #jvm OR #scala OR #groovy OR cloudfoundry OR websockets";
                final SearchResults results = twitterTemplate.searchOperations().search(searchPhrase, 1, 5, sinceId, 0);

               logger.info("sinceId: " + sinceId + "; maxId: " + results.getMaxId());

                sinceId = results.getMaxId();

                final List<TwitterMessage> twitterMessages = new ArrayList<TwitterMessage>();

                for (Tweet tweet : results.getTweets()) {
                    twitterMessages.add(new TwitterMessage(tweet.getId(),
                                                           tweet.getCreatedAt(),
                                                           tweet.getText(),
                                                           tweet.getFromUser(),
                                                           tweet.getProfileImageUrl()));
                }

                return mapper.writeValueAsString(twitterMessages);
            }

        }, 10, TimeUnit.SECONDS);

    }

}

