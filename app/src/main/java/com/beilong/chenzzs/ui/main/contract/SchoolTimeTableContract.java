package com.beilong.chenzzs.ui.main.contract;

import com.beilong.chenzzs.bean.VersionAPI;

import java.util.List;

import czzs.beilong.common.base.BaseModel;
import czzs.beilong.common.base.BasePresenter;
import czzs.beilong.common.base.BaseView;
import rx.Observable;

/**
 * Created by LBL on 2016/11/3.
 */

public interface SchoolTimeTableContract {
    /**
     * 课表改变的RxBus
     */
    public static final String CURRENT_CLASS_NAME_TO_CHANGE = "class_name_to_change";
    /**
     * 改变Toolbar
     */
    public static final String TOOLBAR_TO_CHANGE = "toolbar_to_change";

    interface Model extends BaseModel{
        //解析课表
        Observable<List<String>> getClassTable(String path , String classname);
        //获取服务端版本信息
        Observable<VersionAPI> getVersionAPI();
    }

    interface View extends BaseView{
        //解析课表后的回调
        void returnClassTable(List<String> tables);
        //解析课表失败的回调
        void returnClassTableOnError();
        //检查更新的回调
        void returnCheckVersion(VersionAPI versionAPI);
    }

    abstract static class Presenter extends BasePresenter<View,Model>{
        //获取班级课表
        public abstract void getClassTable(String classname);
        //检查更新
        public abstract void checkVersion();
    }
}
