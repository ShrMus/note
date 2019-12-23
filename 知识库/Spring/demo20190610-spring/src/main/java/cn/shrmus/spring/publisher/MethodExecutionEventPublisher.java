package cn.shrmus.spring.publisher;

import cn.shrmus.spring.event.MethodExecutionEvent;
import cn.shrmus.spring.listener.MethodExecutionEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 组合事件类和监听器，发布事件
 * @Author Shr
 * @CreateDate 2019/6/10 23:12
 * @Version 1.0
 *
 * Spring容器事件发布者
 * @see org.springframework.context.ApplicationContext
 * ApplicationContext实现类AbstractApplicationContext
 * @see org.springframework.context.support.AbstractApplicationContext
 * AbstractApplicationContext让ApplicationEventMulticaster接口完成事件发布
 * @see org.springframework.context.event.ApplicationEventMulticaster
 * ApplicationEventMulticaster实现类AbstractApplicationEventMulticaster
 * @see org.springframework.context.event.AbstractApplicationEventMulticaster
 * AbstractApplicationEventMulticaster将发布功能委派给子类SimpleApplicationEventMulticaster
 * @see org.springframework.context.event.SimpleApplicationEventMulticaster
 * 默认使用SyncTaskExecutor发布事件
 * @see org.springframework.context.event.SimpleApplicationEventMulticaster#setTaskExecutor(java.util.concurrent.Executor)
 */
public class MethodExecutionEventPublisher {
    private List<MethodExecutionEventListener> listeners = new ArrayList<>();

    public void methodToMonitor() {
        MethodExecutionEvent event = new MethodExecutionEvent(this, "methodToMonitor");
        publishEvent(MethodExecutionStatus.BEGIN, event);
        System.out.println("execute the method[methodToMonitor]");
        publishEvent(MethodExecutionStatus.END, event);
    }

    protected void publishEvent(MethodExecutionStatus status,MethodExecutionEvent methodExecutionEvent){
        List<MethodExecutionEventListener> copyListeners = new ArrayList<>(listeners);
        for (MethodExecutionEventListener listener : copyListeners) {
            if(MethodExecutionStatus.BEGIN.equals(status)) {
                listener.onMethodBegin(methodExecutionEvent);
            } else {
                listener.onMethodEnd(methodExecutionEvent);
            }
        }
    }

    public void addMethodExecutionEventListener(MethodExecutionEventListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(MethodExecutionEventListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    public void removeAllListeners(){
        this.listeners.clear();
    }
}
