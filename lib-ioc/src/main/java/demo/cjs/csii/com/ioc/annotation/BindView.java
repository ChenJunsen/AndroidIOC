package demo.cjs.csii.com.ioc.annotation;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述:findViewById注解器
 * <p>
 * 作者:陈俊森
 * 创建时间:2019年08月20日 20:57
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView {
    @IdRes int value();
}
