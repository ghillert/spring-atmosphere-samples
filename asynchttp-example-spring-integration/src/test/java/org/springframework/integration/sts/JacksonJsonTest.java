/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.integration.sts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.integration.support.converter.MessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.Logger;

import com.hillert.websockets.model.TwitterMessage;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JacksonJsonTest {

    @Autowired
    MappingJacksonHttpMessageConverter converter;

    @Test
    public void testSpringIntegrationContextStartup() throws Exception{

        final TwitterMessage message1 = new TwitterMessage(1L, new Date(), "text", "fromUser", "profileImageUrl");
        final TwitterMessage message2 = new TwitterMessage(1L, new Date(), "text", "fromUser", "profileImageUrl");


        final List<TwitterMessage> messages = new ArrayList<TwitterMessage>();
        messages.add(message1);
        messages.add(message2);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(messages);
        System.out.println("json: " + json);
    }

    @Test
    public void testSpringIntegrationContextStartup2() throws Exception{

        final TwitterMessage message1 = new TwitterMessage(1L, new Date(), "text", "fromUser", "profileImageUrl");
        final TwitterMessage message2 = new TwitterMessage(1L, new Date(), "text", "fromUser", "profileImageUrl");


        final List<TwitterMessage> messages = new ArrayList<TwitterMessage>();
        messages.add(message1);
        messages.add(message2);

        ObjectMapper mapper = new ObjectMapper();
        HttpMessageConverter converter = new MappingJacksonHttpMessageConverter();

        MockHttpServletResponse r = new MockHttpServletResponse();
        ServletServerHttpResponse response = new ServletServerHttpResponse(new MockHttpServletResponse());

        converter.write(messages, MediaType.APPLICATION_JSON, response);

        System.out.println(r.getContentAsString());
    }

}
