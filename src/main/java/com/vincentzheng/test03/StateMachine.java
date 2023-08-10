package com.vincentzheng.test03;

import com.vincentzheng.test03.support.DefaultStateMachine;

import java.util.Map;
import java.util.function.Consumer;

/**
 * # todo
 *
 * @author <a href="mailto:wunhwantseng@gmail.com">vincent</a>
 * @since 2023/08/10
 */
public interface StateMachine {

    void fire(String event, String name);

    static StateMachine of(Map<String, Consumer<String>> eventHandler) {
        return new DefaultStateMachine(eventHandler);
    }

}
