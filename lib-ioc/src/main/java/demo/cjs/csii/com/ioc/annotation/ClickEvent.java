package demo.cjs.csii.com.ioc.annotation;

import android.support.annotation.IdRes;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import demo.cjs.csii.com.ioc.Constant;

/**
 * 描述:单击事件注解器
 * <p>
 * 作者:陈俊森
 * 创建时间:2019年08月21日 11:04
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Event(eventClass = View.OnClickListener.class, setter = Constant.Methods.SET_ON_CLICK_LISTENER, methodImpl = Constant.Methods.ON_CLICK)
public @interface ClickEvent {
    @IdRes int[] value();
}
