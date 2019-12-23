package cn.shrmus.spring.listener;

import cn.shrmus.spring.event.MethodExecutionEvent;

/**
 * 自定义事件监听器具体实现类
 * @Author Shr
 * @CreateDate 2019/6/10 23:12
 * @Version 1.0
 */
public class SimpleMethodExecutionEventListener implements MethodExecutionEventListener {
    @Override
    public void onMethodBegin(MethodExecutionEvent event) {
        String methodName = event.getMethodName();
        System.out.println("start to execute the method[" + methodName + "].");
    }

    @Override
    public void onMethodEnd(MethodExecutionEvent event) {
        String methodName = event.getMethodName();
        System.out.println("finished to execute the method[" + methodName + "].");
    }
}
