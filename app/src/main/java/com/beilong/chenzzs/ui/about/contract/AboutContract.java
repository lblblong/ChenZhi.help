package com.beilong.chenzzs.ui.about.contract;

import com.beilong.chenzzs.bean.VersionAPI;

import czzs.beilong.common.base.BaseModel;
import czzs.beilong.common.base.BasePresenter;
import czzs.beilong.common.base.BaseView;
import rx.Observable;

/**
 * Created by LBL on 2016/11/7.
 */

public interface AboutContract {

    interface Model extends BaseModel{
        //获取服务端版本信息
        Observable<VersionAPI> getVersionAPI();
    }

    interface View extends BaseView{
        //检查更新的回调
        void returnCheckVersion(VersionAPI versionAPI);
    }

    abstract static class Presenter extends BasePresenter<View,Model>{
        //检查更新
        public abstract void checkVersion();
    }
}
