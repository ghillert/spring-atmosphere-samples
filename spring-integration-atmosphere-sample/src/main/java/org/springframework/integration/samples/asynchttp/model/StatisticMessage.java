/*
 * Copyright 2002-2012 the original author or authors.
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

import java.util.Collection;

/**
 * @author Gunnar Hillert
 */
public class StatisticMessage extends BaseMessage {

    private Integer numberOfSubscribers;
    private Collection<String>subscriberIds;

    /** Default constructor. */
    public StatisticMessage() {
        super(StatisticMessage.class.getSimpleName());
    }

    public Integer getNumberOfSubscribers() {
		return numberOfSubscribers;
	}

	public void setNumberOfSubscribers(Integer numberOfSubscribers) {
		this.numberOfSubscribers = numberOfSubscribers;
	}

	public Collection<String> getSubscriberIds() {
		return subscriberIds;
	}

	public void setSubscriberIds(Collection<String> subscriberIds) {
		this.subscriberIds = subscriberIds;
	}

	@Override
	public String toString() {
		return "StatisticMessage [numberOfSubscribers=" + numberOfSubscribers
				+ ", subscriberIds=" + subscriberIds + "]";
	}

}
