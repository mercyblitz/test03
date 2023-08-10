package com.vincentzheng.test03;

import com.vincentzheng.test03.annotation.OnAction;
import com.vincentzheng.test03.annotation.OnEvent;
import com.vincentzheng.test03.annotation.WithStateMachine;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * #: todo - what is this
 *
 * @author <a href="mailto:wunhwantseng@gmail.com">vincent</a>
 * @since todo - since from which version
 */
public class StatemachineBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry, BeanNameGenerator nameGenerator) {
        if (!(registry instanceof ListableBeanFactory)) {
            return;
        }
        final ListableBeanFactory beanFactory = (ListableBeanFactory) registry;

        final Map<String, Object> matchBeans = beanFactory.getBeansWithAnnotation(WithStateMachine.class);
        if (CollectionUtils.isEmpty(matchBeans)) {
            return;
        }

        for (Map.Entry<String, Object> entry : matchBeans.entrySet()) {
            final String beanName = entry.getKey();
            final Object beanObject = entry.getValue();
            final Class<?> beanClazz = beanObject.getClass();

            final Map<String, Consumer<String>> maps = new HashMap<>();
            for (Method method : beanClazz.getMethods()) {
                final OnAction onAction = method.getAnnotation(OnAction.class);
                if (onAction == null) {
                    continue;
                }

                final OnEvent onEvent = onAction.onEvent();

                maps.computeIfAbsent(onEvent.value(), key -> name -> {
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, beanObject, name);
                });
            }
            if (CollectionUtils.isEmpty(maps)) {
                continue;
            }


            final StateMachine stateMachine = StateMachine.of(maps);
            final BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(StateMachine.class, () -> stateMachine).getBeanDefinition();

            registry.registerBeanDefinition(nameGenerator.generateBeanName(beanDefinition, registry), beanDefinition);
        }
    }

}
