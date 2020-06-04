package demo.cjs.third.com.ioc.annotation;

import android.support.annotation.IdRes;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import demo.cjs.third.com.ioc.Constant;

/**
 * 描述:长按事件注解器
 * <p>
 * 作者:陈俊森
 * 创建时间:2019年08月21日 11:11
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Event(eventClass = View.OnLongClickListener.class, setter = Constant.Methods.SET_ON_LONG_CLICK_LISTENER, methodImpl = Constant.Methods.ON_LONG_CLICK)
public @interface LongClickEvent {
    @IdRes int[] value();
    boolean consume();
}
