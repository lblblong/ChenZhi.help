package czzs.beilong.common.baseapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by LBL on 2016/10/30.
 */

public class BaseApplication extends Application{
    private static BaseApplication baseApplication;
    private static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        NoHttp.initialize(this);
    }

    public static RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = NoHttp.newRequestQueue();
        }
        return requestQueue;
    }

    public static Context getAppContext(){
        return baseApplication;
    }

    public static Resources getAppResources(){
        return baseApplication.getResources();
    }

}
