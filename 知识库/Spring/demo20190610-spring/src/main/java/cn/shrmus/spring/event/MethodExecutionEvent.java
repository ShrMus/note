package cn.shrmus.spring.event;

import java.util.EventObject;

/**
 * 自定义事件类型
 * @Author Shr
 * @CreateDate 2019/6/10 23:05
 * @Version 1.0
 *
 * Spring容器内自定义事件类型
 * @see org.springframework.context.ApplicationEvent
 */
public class MethodExecutionEvent extends EventObject {
    private String methodName;

    public MethodExecutionEvent(Object source) {
        super(source);
    }

    public MethodExecutionEvent(Object source,String methodName) {
        super(source);
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
