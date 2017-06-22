package czzs.beilong.common.baseapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by LBL on 2016/10/29.
 */

public class AppManager {
    private static Stack<Activity> activityStack;   //后进先出的对象堆栈
    private volatile static AppManager instance;

    private AppManager(){}
    /**
     * 单一实例
     */
    public static AppManager getInstance(){
        if (instance == null){
            instance = new AppManager();
            instance.activityStack = new Stack<>();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     * @param activity
     */
    public void addActivity(Activity activity){
        if (activityStack == null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        try {
            Activity activity = activityStack.lastElement();
            return activity;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取当前Activity的前一个Activity
     * @return
     */
    public Activity preActivity(){
        int index = activityStack.size() - 2;
        if(index < 0){
            return null;
        }
        Activity activity = activityStack.get(index);
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if (activity != null){
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移除指定的Activity
     * @param activity
     */
    public void removeActiity(Activity activity){
        if (activity != null){
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     * @param cls
     */
    public void finishActivity(Class<?> cls){
        try{
            for (Activity activity : activityStack){
                if (activity.getClass().equals(cls));
                finishActivity(activity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        for (int i = 0 , size = activityStack.size() ; i<size ; i++){
            if(activityStack.get(i) != null){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 返回到指定的Activity
     * @param cls
     */
    public void returnToActivity(Class<?> cls){
        while(activityStack.size() != 0){
            if (activityStack.peek().getClass() == cls){
                break;
            }else {
                finishActivity(activityStack.peek());
            }
        }
    }

    /**
     * 是否已经打开指定的Activity
     * @param cls
     * @return
     */
    public boolean isOpenActivity(Class<?> cls){
        if (activityStack != null){
            for (int i = 0,size = activityStack.size();i<size;i++){
                if (cls == activityStack.get(i).getClass()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     * @param context   上下文
     * @param isBackground  是否开启后台运行
     */
    public void AppExit(Context context, Boolean isBackground){
        try{
            finishActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
        }catch (Exception e){
        }finally {
            //如果有后台程序运行，请不要支持此句子
            if(!isBackground){
                System.exit(0);
            }
        }
    }
}
