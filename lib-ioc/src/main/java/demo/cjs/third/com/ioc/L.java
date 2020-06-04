package demo.cjs.third.com.ioc;

import android.util.Log;

/**
 * 描述:日志辅助工具类
 * <p>
 * 作者:陈俊森
 * 创建时间:2019年08月21日 9:27
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class L {
    /**
     * the default log tag
     */
    public static final String TAG = "IOC-Inject";
    /**
     * the log switch true-open false-close
     */
    public static boolean isOpen = true;

    public static void v(String tag, String msg) {
        if (isOpen) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isOpen) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isOpen) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isOpen) {
            Log.w(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isOpen) {
            Log.i(tag, msg);
        }
    }

    public static void sysout(String tag, String msg) {
        if (isOpen) {
            System.out.println(String.format("[%1$s]%2$s", tag, msg));
        }
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void sysout(String msg) {
        sysout(TAG, msg);
    }
}
