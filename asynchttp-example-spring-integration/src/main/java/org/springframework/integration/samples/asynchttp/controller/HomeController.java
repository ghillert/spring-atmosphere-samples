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
package org.springframework.integration.samples.asynchttp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.asynchttp.BroadcasterManager;
import org.springframework.integration.samples.asynchttp.service.TwitterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private BroadcasterManager broadcasterManager;
    
    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value="/")
    public String home(Model model) {
        return "home";
    }

    @RequestMapping(value="/timeservice")
    @ResponseBody
    public String subscribeTimeChannel(HttpSession session, @RequestParam(value="subscribe", required=false) boolean subscribe) {	
    	
    	Broadcaster globalBroadcaster = broadcasterManager.getDefaultBroadcaster();
    	
    	Broadcaster timeBroadcaster = broadcasterManager.getBroadcaster("timeBroadcaster");

    	BroadcasterFactory.getDefault().lookupAll();
    	for (AtmosphereResource<?, ?> resource : globalBroadcaster.getAtmosphereResources()) {
    		if (((HttpServletRequest)resource.getRequest()).getSession().getId().equals(session.getId())) {
    			if (subscribe) {
    			   timeBroadcaster.addAtmosphereResource(resource);
    			   return("Subscribed");
    			} else {
    			   timeBroadcaster.removeAtmosphereResource(resource);
    			   return("Unsubscribed.");
    			}
    		}
    	}
    	
        return "Nothing Happened.";
    }
    
}

