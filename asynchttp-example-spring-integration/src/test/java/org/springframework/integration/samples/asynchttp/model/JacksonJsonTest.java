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
package org.springframework.integration.samples.asynchttp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JacksonJsonTest {

    @Autowired
    MappingJacksonHttpMessageConverter converter;

    @Test
    public void testSerializeTwitterMessageToJson() throws Exception{

    	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
    	
        final TwitterMessage message1 = new TwitterMessage(1L, getSampleDate(), "text", "fromUser", "profileImageUrl");
        final TwitterMessage message2 = new TwitterMessage(1L, getSampleDate(), "text", "fromUser", "profileImageUrl");


        final List<TwitterMessage> messages = new ArrayList<TwitterMessage>();
        messages.add(message1);
        messages.add(message2);

        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(messages);
        
        final String expectedJson = "[{\"id\":1,\"text\":\"text\",\"createdAt\":1309898277000,\"fromUser\":\"fromUser\",\"profileImageUrl\":\"profileImageUrl\"},{\"id\":1,\"text\":\"text\",\"createdAt\":1309898277000,\"fromUser\":\"fromUser\",\"profileImageUrl\":\"profileImageUrl\"}]";
        
        System.out.println("expectedJson: " + expectedJson);
        System.out.println("json        : " + json);
        
        Assert.assertEquals(expectedJson,json);

    }

    @Test
    public void testSerializeTwitterMessageToJsonUsingHttpConverter() throws Exception{

        final TwitterMessage message1 = new TwitterMessage(1L, getSampleDate(), "text", "fromUser", "profileImageUrl");
        final TwitterMessage message2 = new TwitterMessage(1L, getSampleDate(), "text", "fromUser", "profileImageUrl");


        final List<TwitterMessage> messages = new ArrayList<TwitterMessage>();
        messages.add(message1);
        messages.add(message2);

        final HttpMessageConverter<Object> converter = new MappingJacksonHttpMessageConverter();

        final MockHttpServletResponse r = new MockHttpServletResponse();
        final ServletServerHttpResponse response = new ServletServerHttpResponse(r);

        converter.write(messages, MediaType.APPLICATION_JSON, response);

        final String expectedJson = "[{\"id\":1,\"text\":\"text\",\"createdAt\":1309898277000,\"fromUser\":\"fromUser\",\"profileImageUrl\":\"profileImageUrl\"},{\"id\":1,\"text\":\"text\",\"createdAt\":1309898277000,\"fromUser\":\"fromUser\",\"profileImageUrl\":\"profileImageUrl\"}]";
        
        System.out.println("expectedJson: " + expectedJson);
        System.out.println("json        : " + r.getContentAsString());
        
        Assert.assertEquals(expectedJson,r.getContentAsString());

    }

    private Date getSampleDate() throws Exception {
    	return getDate("2011-07-05T16:37:57EDT") ;
    }
    
    private Date getDate(String dateAsString) throws Exception {
    	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
    	return dateFormat.parse("2011-07-05T16:37:57EDT");
    }
    
}
