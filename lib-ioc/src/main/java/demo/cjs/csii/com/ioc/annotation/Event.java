package demo.cjs.csii.com.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述:通用时间注解器
 * <p>
 * 作者:陈俊森
 * 创建时间:2019年08月21日 10:57
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Event {
    /**
     * 需要处理的事件的类,比如“View.OnClickListener.class”
     */
    Class<?> eventClass();

    /**
     * 获取这个类的方法名字，比如“setOnClickListener”
     */
    String setter();

    /**
     * 这个类里面需要实现的方法名字,比如“onClick”
     */
    String methodImpl();

}
