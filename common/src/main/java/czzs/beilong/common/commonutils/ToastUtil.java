package czzs.beilong.common.commonutils;

import android.content.Context;
import android.widget.Toast;

import czzs.beilong.common.baseapp.BaseApplication;

/**
 * Created by LBL on 2016/10/29.
 * Toast统一管理类
 */

public class ToastUtil{
    private static Toast toast;

    private static Toast initToast(CharSequence message,int duration){
        if (toast == null){
            toast = Toast.makeText(BaseApplication.getAppContext(),message,duration);
        }else{
            toast.setText(message);
        }
        return toast;
    }

    /**
     * --------------------------------------------------------------------------------
     * 短时间Toast
     * @param message
     */
    public static void showShort(CharSequence message){
        initToast(message,Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast（资源中定义的）
     * @param strResId
     */
    public static void showShort(int strResId){
        initToast(BaseApplication.getAppContext().getResources().getText(strResId),Toast.LENGTH_SHORT);
    }

    /**
     * -------------------------------------------------------------------------
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        initToast(message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast（资源中定义的）
     *
     * @param strResId
     */
    public static void showLong(int strResId) {
        initToast(BaseApplication.getAppContext().getResources().getText(strResId), Toast.LENGTH_LONG).show();
    }

    /**
     * -----------------------------------------------------------------------------
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        initToast(message, duration).show();
    }

    /**
     * 自定义显示Toast时间（资源中定义的）
     *
     * @param context
     * @param strResId
     * @param duration
     */
    public static void show(Context context, int strResId, int duration) {
        initToast(context.getResources().getText(strResId), duration).show();
    }

}
