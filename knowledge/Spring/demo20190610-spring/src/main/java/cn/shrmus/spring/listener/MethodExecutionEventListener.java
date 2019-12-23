package cn.shrmus.spring.listener;

import cn.shrmus.spring.event.MethodExecutionEvent;
import java.util.EventListener;

/**
 * 针对自定义事件的监听器接口
 * @Author Shr
 * @CreateDate 2019/6/10 23:11
 * @Version 1.0
 *
 * Spring容器自定义事件监听器接口
 * @see org.springframework.context.ApplicationListener
 */
public interface MethodExecutionEventListener extends EventListener {
    /**
     * 处理方法开始执行的时候发布的MethodExecutionEvent事件
     * @param event
     */
    void onMethodBegin(MethodExecutionEvent event);
    /**
     * 处理方法执行结束的时候发布的MethodExecutionEvent事件
     * @param event
     */
    void  onMethodEnd(MethodExecutionEvent event);
}
