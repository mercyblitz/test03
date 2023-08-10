/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vincentzheng.test03.event;

import com.vincentzheng.test03.annotation.OnAction;
import com.vincentzheng.test03.annotation.OnEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * {@link OnAction} Method {@link EventListenerFactory}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class OnActionEventListenerFactory implements EventListenerFactory, BeanFactoryAware, Ordered {

    private BeanFactory beanFactory;

    @Override
    public boolean supportsMethod(Method method) {
        return method.getDeclaredAnnotation(OnAction.class) != null;
    }

    @Override
    public ApplicationListener<?> createApplicationListener(String beanName,
                                                            Class<?> beanType,
                                                            Method method) {
        OnAction onAction = method.getDeclaredAnnotation(OnAction.class);
        OnEvent onEvent = onAction.onEvent();
        String eventValue = onEvent.value();
        // eventValue -> Bean
        //
        Object beanObject = beanFactory.getBean(beanName, beanType);
        // @OnAction(@OnEvent("...") -> Method -> OnActionEventListener(Method,Bean Object)
        return new OnActionEventListener(method, beanObject);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
