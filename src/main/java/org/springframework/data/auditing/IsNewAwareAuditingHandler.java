/*
 * Copyright 2012-2014 the original author or authors.
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
package org.springframework.data.auditing;

import org.springframework.data.support.IsNewStrategy;
import org.springframework.data.support.IsNewStrategyFactory;
import org.springframework.util.Assert;

/**
 * {@link AuditingHandler} extension that uses an {@link IsNewStrategyFactory} to expose a generic
 * {@link #markAudited(Object)} method that will route calls to {@link #markCreated(Object)} or
 * {@link #markModified(Object)} based on the {@link IsNewStrategy} determined from the factory.
 * 
 * @author Oliver Gierke
 * @since 1.5
 */
public class IsNewAwareAuditingHandler extends AuditingHandler {

	private final IsNewStrategyFactory isNewStrategyFactory;

	/**
	 * Creates a new {@link IsNewAwareAuditingHandler} using the given {@link IsNewStrategyFactory}.
	 * 
	 * @param isNewStrategyFactory must not be {@literal null}.
	 */
	public IsNewAwareAuditingHandler(IsNewStrategyFactory isNewStrategyFactory) {

		Assert.notNull(isNewStrategyFactory, "IsNewStrategy must not be null!");
		this.isNewStrategyFactory = isNewStrategyFactory;
	}

	/**
	 * Marks the given object created or modified based on the {@link IsNewStrategy} returned by the
	 * {@link IsNewStrategyFactory} configured. Will rout the calls to {@link #markCreated(Object)} and
	 * {@link #markModified(Object)} accordingly.
	 * 
	 * @param object
	 */
	public void markAudited(Object object) {

		if (object == null) {
			return;
		}

		IsNewStrategy strategy = isNewStrategyFactory.getIsNewStrategy(object.getClass());

		if (strategy.isNew(object)) {
			markCreated(object);
		} else {
			markModified(object);
		}
	}
}
