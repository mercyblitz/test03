package com.vincentzheng.test03;

import com.vincentzheng.test03.annotation.OnAction;
import com.vincentzheng.test03.annotation.OnEvent;
import com.vincentzheng.test03.annotation.WithStateMachine;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;
import static org.springframework.util.ClassUtils.isPresent;
import static org.springframework.util.ClassUtils.resolveClassName;

/**
 * #: Solution B
 *
 * @author <a href="mailto:wunhwantseng@gmail.com">vincent</a>
 * @since todo - since from which version
 */

public class StatemachineBeanDefinitionRegistrar2 implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry, BeanNameGenerator nameGenerator) {
        if (!(registry instanceof ConfigurableListableBeanFactory)) {
            return;
        }

        final ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) registry;

        // 提早初始化
        // BeanFactory BeanProcessor = ApplicationContextAwareProcessor + ApplicationListenerDetector
        // final Map<String, Object> matchBeans = beanFactory.getBeansWithAnnotation(WithStateMachine.class);
        String[] beanNames = beanFactory.getBeanNamesForAnnotation(WithStateMachine.class);
        // testA.A -> BeanFactory.singletonObjects -> "testA.A" - A@123123213
//        if (CollectionUtils.isEmpty(matchBeans)) {
//            return;
//        }
        if (ObjectUtils.isEmpty(beanNames)) {
            return;
        }

        ClassLoader classLoader = beanFactory.getBeanClassLoader();
//        for (Map.Entry<String, Object> entry : matchBeans.entrySet()) {
//            final String beanName = entry.getKey();
//            final Object beanObject = entry.getValue();
//            final Class<?> beanClazz = beanObject.getClass();
//        }

        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (isPresent(beanClassName, classLoader)) {
                Class<?> beanClass = resolveClassName(beanClassName, classLoader);
                final Map<String, Consumer<String>> maps = new HashMap<>();
                for (Method method : beanClass.getMethods()) {
                    final OnAction onAction = method.getAnnotation(OnAction.class);
                    if (onAction == null) {
                        continue;
                    }

                    final OnEvent onEvent = onAction.onEvent();

                    ReflectionUtils.makeAccessible(method);

                    maps.computeIfAbsent(onEvent.value(), key -> name -> {
                        Object beanObject = beanFactory.getBean(beanName, beanClass);
                        ReflectionUtils.invokeMethod(method, beanObject, name);
                    });
                }
                if (CollectionUtils.isEmpty(maps)) {
                    continue;
                }


                final StateMachine stateMachine = StateMachine.of(maps);
                final BeanDefinition stateMachineBeanDefinition = genericBeanDefinition(StateMachine.class, () -> stateMachine).getBeanDefinition();
                registry.registerBeanDefinition(nameGenerator.generateBeanName(beanDefinition, registry), stateMachineBeanDefinition);
            }
        }


    }

}
