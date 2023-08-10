package com.vincentzheng.test03;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * #: todo - what is this
 *
 * @author <a href="mailto:wunhwantseng@gmail.com">vincent</a>
 * @since todo - since from which version
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Configuration(proxyBeanMethods = false)
@Import(StatemachineBeanDefinitionRegistrar.class)
public @interface EnableStatemachine {

}
