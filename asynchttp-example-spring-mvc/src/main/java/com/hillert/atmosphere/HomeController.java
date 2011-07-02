package com.hillert.atmosphere;

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
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hillert.atmosphere.model.TwitterMessage;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home() {
        logger.info("Welcome home!");
        return "home";
    }

    /**
     *
     */
    @RequestMapping(value="/websockets", method=RequestMethod.GET)
    @ResponseBody
    public void websockets(final AtmosphereResource<HttpServletRequest,HttpServletResponse> event) {

        final HttpServletRequest  req = event.getRequest();
        final HttpServletResponse res = event.getResponse();

        final ObjectMapper mapper = new ObjectMapper();

        event.suspend();

        final Broadcaster bc = event.getBroadcaster();

        bc.scheduleFixedBroadcast(new Callable<String>() {

            private long sinceId = 0;

            @Override
            public String call() throws Exception {

                final TwitterTemplate twitterTemplate = new TwitterTemplate();
                final SearchResults results = twitterTemplate.searchOperations().search("world", 1, 5, sinceId, 0);

               logger.info("sinceId: " + sinceId + "; maxId: " + results.getMaxId());

                sinceId = results.getMaxId();

                List<TwitterMessage> twitterMessages = new ArrayList<TwitterMessage>();

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

        //bc.delayBroadcast("Underlying Response now suspended");

    }

}

