package com.vincentzheng.test03;

import com.vincentzheng.test03.annotation.OnAction;
import com.vincentzheng.test03.annotation.OnEvent;
import com.vincentzheng.test03.annotation.WithStateMachine;
import com.vincentzheng.test03.event.OnActionEventListenerFactory;
import com.vincentzheng.test03.support.EventBasedStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * # todo
 *
 * @author <a href="mailto:wunhwantseng@gmail.com">vincent</a>
 * @since 2023/08/10
 */
public class TestA {

    public static void main(String[] args) {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
//
//        StateMachine machine = applicationContext.getBean(StateMachine.class);
//        // Solution A
////        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
////        beanFactory.getBeansWithAnnotation(WithStateMachine.class)
////                .values().forEach(beanFactory::autowireBean);
//        machine.fire("xx", "zhangsan");

        // Solution C
        executeSolutionC();

        A a = null;
        // CommonService ClassLoader Common #0
        // A.class ClassLoader 1 #1
        // A.class ClassLoader 2 #2
        // ClassCastException A is not A
        // Spring Boot Devtools
    }

    private static void executeSolutionC() {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(
                        TestConfiguration.class,
                        OnActionEventListenerFactory.class,
                        EventBasedStateMachine.class);

        StateMachine machine = applicationContext.getBean(StateMachine.class);

        machine.fire("xx", "wangwu");
    }

    // @EnableStatemachine
    @Configuration(proxyBeanMethods = false)
    @ComponentScan("com.vincentzheng.test03")
    private static class TestConfiguration {

    }

    @Component
    @WithStateMachine("a_machine")
    private static class A {

        private B b;

        /**
         * A(Bean) -> doExecute ->  @OnAction(onEvent = @OnEvent("xx"))
         * "xx" -> A(Bean) -> A(Bean) + Method = Java Reflection -> method.invoke(beanObject,name)
         */
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
