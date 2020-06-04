package demo.cjs.third.com.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 描述:
 * <p>
 * 作者:陈俊森
 * 创建时间:2019年08月21日 14:24
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class InjectInvocationHandler implements InvocationHandler {
    private Object mParent;
    private Map<String, Method> mMethodMap;

    private Object mInvokeResult;

    public InjectInvocationHandler(Object parent, Map<String, Method> methodMap) {
        mParent = parent;
        mMethodMap = methodMap;
    }

    public void setInvokeResult(Object invokeResult) {
        mInvokeResult = invokeResult;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (mParent != null && mMethodMap != null) {
            String methodName = method.getName();
            Method method1 = mMethodMap.get(methodName);
            if (method1 != null) {
                if (mInvokeResult != null) {
                    method1.invoke(mParent, args);
                    return mInvokeResult;
                } else {
                    return method1.invoke(mParent, args);
                }
            }
        } else {
            L.e("InjectInvocationHandler invoke failed case the parent or the methodMap is null");
        }
        return null;
    }
}
