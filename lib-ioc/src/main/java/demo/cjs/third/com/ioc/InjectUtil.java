package demo.cjs.third.com.ioc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import demo.cjs.third.com.ioc.annotation.BindView;
import demo.cjs.third.com.ioc.annotation.ContentViewId;
import demo.cjs.third.com.ioc.annotation.Event;

/**
 * 描述:注解器工具
 * <p>
 * 作者:陈俊森
 * 创建时间:2019年08月20日 19:55
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class InjectUtil {
    /**
     * 执行注解器(Activity专用)
     * @param activity 宿主Activity
     */
    public static void inject(Activity activity) {
        L.d("------------prepare inject--------------");
        if (activity == null) {
            L.e("inject failed,case the activity is null");
            return;
        } else {
            L.d("The activity to be injected is " + activity);
        }
        injectContentViewId(activity);
        injectFindViewById(activity);
        injectOnClickListener(activity, activity);
        L.d("------------inject finished--------------");
    }

    /**
     * 反射注入setContentView(contentId)
     *
     * @param activity
     */
    private static void injectContentViewId(Activity activity) {
        L.d("1.start injectContentViewId");
        Class<? extends Activity> clazz = activity.getClass();
        ContentViewId contentViewIdAnnotation = clazz.getAnnotation(ContentViewId.class);
        //if there exists the annotation,then reflect the setContentView method and invoke the contentId
        if (contentViewIdAnnotation != null) {
            int contentId = contentViewIdAnnotation.value();
            try {
                Method setContentViewMethod = clazz.getMethod(Constant.Methods.SET_CONTENT_VIEW, int.class);
                setContentViewMethod.setAccessible(true);
                //-->activity.setContentView(contentId)
                setContentViewMethod.invoke(activity, contentId);
            } catch (Exception e) {
                e.printStackTrace();
                L.v(e.getMessage());
            }
        }
        L.d("1.end injectContentViewId");
    }

    /**
     * 反射注入findViewById
     *
     * @param activity
     */
    private static void injectFindViewById(Activity activity) {
        L.d("2.start injectFindViewById");
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                BindView bindViewAnnotation = field.getAnnotation(BindView.class);
                if (bindViewAnnotation != null) {
                    try {
                        Method bindViewMethod = clazz.getMethod(Constant.Methods.FIND_VIEW_BY_ID, int.class);
                        bindViewMethod.setAccessible(true);
                        field.setAccessible(true);
                        int viewId = bindViewAnnotation.value();
                        Object view = bindViewMethod.invoke(activity, viewId);
                        //-->activity.v=activity.findViewById(viewId)
                        field.set(activity, view);
                    } catch (Exception e) {
                        e.printStackTrace();
                        L.v(e.getMessage());
                    }
                }
            }
        } else {
            L.d("injectFindViewById failed,case " + activity + "has no fields");
        }
        L.d("2.end injectFindViewById");
    }

    /**
     * 反射注入单击和长按事件的监听
     *
     * @param activity
     */
    private static void injectOnClickListener(Activity activity) {
        L.d("3.start injectOnClickListener");
        Class<? extends Activity> clazz = activity.getClass();
        //考虑到反射的性能问题，这里采用的是getDeclaredMethods而不是getMethods方法。后者会获取除自身内部定义的方法外，还会获取继承的类的方法。而前者只会获取自身方法，方法数量大大减少，增加效率
        Method[] methods = clazz.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                L.i("--annotate method:" + method.getName());
                Annotation[] annotations = method.getAnnotations();//get the public annotations of the method
                if (annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        Class<? extends Annotation> annotationType = annotation.annotationType();
                        L.i("----annotate annotation:" + annotationType.getName());
                        try {
                            Method valueMethod = annotationType.getDeclaredMethod("value");
                            int[] ids = (int[]) valueMethod.invoke(annotation);//get the ids that need to be set onClickListener
                            if (ids != null && ids.length > 0) {
                                Event eventAnnotation = annotationType.getAnnotation(Event.class);//get the ClickEvent annotation's Event annotation
                                if (eventAnnotation != null) {
                                    //get the clickEvent params
                                    Class<?> clickEventClass = eventAnnotation.eventClass();
                                    String clickMethodImpl = eventAnnotation.methodImpl();
                                    String clickSetter = eventAnnotation.setter();
                                    Map<String, Method> methodMap = new HashMap<>(1);
                                    methodMap.put(clickMethodImpl, method);
                                    InjectInvocationHandler handler = new InjectInvocationHandler(activity, methodMap);
                                    //针对长按事件做特殊的带返回值的处理
                                    if (Constant.Methods.SET_ON_LONG_CLICK_LISTENER.equals(clickSetter)) {
                                        Method consumeMethod = annotationType.getDeclaredMethod("consume");
                                        if (consumeMethod != null) {
                                            boolean consume = (boolean) consumeMethod.invoke(annotation);//get the consume property value
                                            handler.setInvokeResult(consume);
                                        } else {
                                            handler.setInvokeResult(false);
                                        }
                                    }
                                    Object proxy = Proxy.newProxyInstance(clickEventClass.getClassLoader(), new Class<?>[]{clickEventClass}, handler);
                                    for (int id : ids) {
                                        View v = activity.findViewById(id);
                                        if (v != null) {
                                            Method vMethod = v.getClass().getMethod(clickSetter, clickEventClass);
                                            vMethod.invoke(v, proxy);
                                        } else {
                                            L.e("Oops! The view inflated by id " + id + " is not exists!");
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            L.v(e.getMessage());
                        }

                    }
                }
            }
            L.d("3.end injectOnClickListener");
        } else {
            L.d("injectOnClickListener failed case " + activity + " has no public methods");
        }
    }

    /**
     * 执行注解器(非Activity)
     * @param parent
     * @param contentView
     */
    public static void inject(Object parent, View contentView) {
        if (parent != null) {
            if (contentView == null) {
                if (parent instanceof Fragment) {
                    contentView = ((Fragment) parent).getView();
                }
            }
            if (contentView == null) {
                throw new IllegalArgumentException("Inject failed,you can't inject with a null contentView");
            } else {
                injectFindViewById(parent, contentView);
                injectOnClickListener(parent, contentView);
            }
        } else {
            throw new IllegalArgumentException("Inject failed,you can't inject with a null parent");
        }
    }

    private static void injectFindViewById(Object parent, Object contentView) {
        Class<?> clazzF = parent.getClass();
        Field[] fields = clazzF.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                field.setAccessible(true);
                L.i("injectFindViewById field:" + field.getName());
                BindView bindViewAnnotation = field.getAnnotation(BindView.class);
                if (bindViewAnnotation != null) {
                    int id = bindViewAnnotation.value();
                    try {
                        Method method = contentView.getClass().getMethod(Constant.Methods.FIND_VIEW_BY_ID, int.class);
                        Object view = method.invoke(contentView, id);
                        field.set(parent, view);
                    } catch (Exception e) {
                        e.printStackTrace();
                        L.v(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 反射注入单击和长按事件的监听
     * @param parent
     * @param contentView
     */
    private static void injectOnClickListener(Object parent, Object contentView) {
        L.d("3.start injectOnClickListener");
        Class<?> clazz = parent.getClass();
        //考虑到反射的性能问题，这里采用的是getDeclaredMethods而不是getMethods方法。后者会获取除自身内部定义的方法外，还会获取继承的类的方法。而前者只会获取自身方法，方法数量大大减少，增加效率
        Method[] methods = clazz.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                L.i("--annotate method:" + method.getName());
                Annotation[] annotations = method.getAnnotations();//get the public annotations of the method
                if (annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        Class<? extends Annotation> annotationType = annotation.annotationType();
                        L.i("----annotate annotation:" + annotationType.getName());
                        try {
                            Method valueMethod = annotationType.getDeclaredMethod("value");
                            int[] ids = (int[]) valueMethod.invoke(annotation);//get the ids that need to be set onClickListener
                            if (ids != null && ids.length > 0) {
                                Event eventAnnotation = annotationType.getAnnotation(Event.class);//get the ClickEvent annotation's Event annotation
                                if (eventAnnotation != null) {
                                    //get the clickEvent params
                                    Class<?> clickEventClass = eventAnnotation.eventClass();
                                    String clickMethodImpl = eventAnnotation.methodImpl();
                                    String clickSetter = eventAnnotation.setter();
                                    Map<String, Method> methodMap = new HashMap<>(1);
                                    methodMap.put(clickMethodImpl, method);
                                    InjectInvocationHandler handler = new InjectInvocationHandler(parent, methodMap);
                                    //针对长按事件做特殊的带返回值的处理
                                    if (Constant.Methods.SET_ON_LONG_CLICK_LISTENER.equals(clickSetter)) {
                                        Method consumeMethod = annotationType.getDeclaredMethod("consume");
                                        if (consumeMethod != null) {
                                            boolean consume = (boolean) consumeMethod.invoke(annotation);//get the consume property value
                                            handler.setInvokeResult(consume);
                                        } else {
                                            handler.setInvokeResult(false);
                                        }
                                    }
                                    Object proxy = Proxy.newProxyInstance(clickEventClass.getClassLoader(), new Class<?>[]{clickEventClass}, handler);
                                    for (int id : ids) {
                                        Method contentFindMethod = contentView.getClass().getMethod(Constant.Methods.FIND_VIEW_BY_ID, int.class);
                                        Object v = contentFindMethod.invoke(contentView, id);
//                                        View v = contentView.findViewById(id);
                                        if (v != null) {
                                            Method vMethod = v.getClass().getMethod(clickSetter, clickEventClass);
                                            vMethod.invoke(v, proxy);
                                        } else {
                                            L.e("Oops! The view inflated by id " + id + " is not exists!");
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            L.v(e.getMessage());
                        }

                    }
                }
            }
            L.d("3.end injectOnClickListener");
        } else {
            L.d("injectOnClickListener failed case " + parent + " has no public methods");
        }
    }

    /**
     * 运行注解器(Fragment专用，确保使用该方法时，{@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}已经成功返回contentView,建议在{@link Fragment#onViewCreated(View, Bundle)}中使用)
     *
     * @param fragment 宿主Fragment
     */
    public static void inject(Fragment fragment) {
        inject(fragment, null);
    }
}
