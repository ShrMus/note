package cn.shrmus.spring.app;

import cn.shrmus.spring.listener.SimpleMethodExecutionEventListener;
import cn.shrmus.spring.publisher.MethodExecutionEventPublisher;

public class Main {
    public static void main(String[] args) {
        MethodExecutionEventPublisher eventPublisher = new MethodExecutionEventPublisher();
        eventPublisher.addMethodExecutionEventListener(new SimpleMethodExecutionEventListener());
        eventPublisher.methodToMonitor();
    }
}
