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
package org.springframework.integration.samples.asynchttp.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.integration.samples.asynchttp.model.TimeMessage;
import org.springframework.integration.samples.asynchttp.service.TimeService;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Gunnar Hillert
 *
 */
@Service(value="timeService")
public class DefaultTimeService implements TimeService {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TimeMessage getCurrentTime() {
		Date date = new Date();
		
		TimeMessage message = new TimeMessage();
		message.setTime(dateFormat.format(date));
		
		return message;
	}
}
