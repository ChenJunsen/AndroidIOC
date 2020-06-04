package demo.cjs.third.com.ioc.annotation;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述:设置Activity布局的LayoutId
 * <p>
 * 作者:陈俊森
 * 创建时间:2019年08月20日 19:50
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentViewId {
    //    int id();
    //我们可以在该属性前面调用一个@LayoutRes来提醒操作者必须传入Layout的布局id
    @LayoutRes int value();//注解的规范要求，当只有一个属性的时候建议使用value作为属性名字，当然也可以使用上面的id,不过一旦使用非value的属性后，使用时需要显式地调用
}
