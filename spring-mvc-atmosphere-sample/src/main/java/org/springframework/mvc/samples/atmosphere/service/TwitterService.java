package org.springframework.mvc.samples.atmosphere.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.MetaBroadcaster;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.samples.atmosphere.AtmosphereUtils;
import org.springframework.mvc.samples.atmosphere.model.TwitterMessage;
import org.springframework.mvc.samples.atmosphere.model.TwitterMessages;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

public class TwitterService {

	@Autowired
	private ObjectMapper objectMapper;

	private volatile long sinceId = 0;

	private static final Logger logger = LoggerFactory
			.getLogger(TwitterService.class);

	@Scheduled(fixedDelay=5000)
	public void pollForTwitterMessages() {

		final TwitterTemplate twitterTemplate = new TwitterTemplate();
		final String searchPhrase = "springintegration OR html5 OR vfabric OR #springframework OR springsource OR #java OR #eip OR infoq OR #jvm OR #scala OR cloudfoundry OR websockets";
		final SearchResults results = twitterTemplate
				.searchOperations().search(searchPhrase, 1, 5, sinceId,
						0);

		logger.info("sinceId: " + sinceId + "; maxId: "
				+ results.getMaxId());

		sinceId = results.getMaxId();

		final List<TwitterMessage> twitterMessages = new ArrayList<TwitterMessage>();

		for (Tweet tweet : results.getTweets()) {
			twitterMessages.add(new TwitterMessage(tweet.getId(), tweet
					.getCreatedAt(), tweet.getText(), tweet
					.getFromUser(), tweet.getProfileImageUrl()));
		}

		TwitterMessages twitterMessageWrapper = new TwitterMessages();
		twitterMessageWrapper.setTwitterMessages(twitterMessages);

		try {
			MetaBroadcaster.getDefault().broadcastTo("/", objectMapper.writeValueAsString(twitterMessageWrapper));
		} catch (JsonGenerationException e) {
			throw new IllegalStateException(e);
		} catch (JsonMappingException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

}
