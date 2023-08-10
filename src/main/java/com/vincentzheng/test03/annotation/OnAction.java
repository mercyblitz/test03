package com.vincentzheng.test03.annotation;

import com.vincentzheng.test03.event.OnActionEvent;
import org.springframework.context.event.EventListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * #: todo - what is this
 *
 * @author wunhwantseng@gmail.com
 * @since todo - since from which version
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventListener(OnActionEvent.class)
public @interface OnAction {

    OnEvent onEvent();

}
