package com.vincentzheng.test03;

import com.vincentzheng.test03.annotation.OnAction;
import com.vincentzheng.test03.annotation.OnEvent;
import com.vincentzheng.test03.annotation.WithStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * # todo
 *
 * @author <a href="mailto:wunhwantseng@gmail.com">vincent</a>
 * @since 2023/08/10
 */
public class TestA {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);

        StateMachine machine = applicationContext.getBean(StateMachine.class);
        machine.fire("xx","zhangsan");
    }

    @EnableStatemachine
    @Configuration(proxyBeanMethods = false)
    @ComponentScan("com.vincentzheng.test03")
    private static class TestConfiguration {

    }

    @Component
    @WithStateMachine("a_machine")
    private static class A {

        private B b;

        @OnAction(onEvent = @OnEvent("xx"))
        public void doExecute(String name) {
            System.out.println("B: " + b);
            System.out.println("name: " + name);
        }

        @Autowired
        public void setB(B b) {
            this.b = b;
        }
    }

    @Component
    public static class B {


    }

}
