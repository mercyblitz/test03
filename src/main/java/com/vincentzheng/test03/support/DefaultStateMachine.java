package com.vincentzheng.test03.support;

import com.vincentzheng.test03.StateMachine;

import java.util.Map;
import java.util.function.Consumer;

/**
 * # todo
 *
 * @author <a href="mailto:wunhwantseng@gmail.com">vincent</a>
 * @since 2023/08/10
 */
public class DefaultStateMachine implements StateMachine {

    private final Map<String, Consumer<String>> eventHandler;

    public DefaultStateMachine(Map<String, Consumer<String>> eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void fire(String event, String name) {
        Consumer<String> handler = eventHandler.get(event);
        if (handler != null) {
            handler.accept(name);
        }
    }
}
