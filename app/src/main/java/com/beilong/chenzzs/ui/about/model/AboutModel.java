package com.beilong.chenzzs.ui.about.model;

import com.beilong.chenzzs.api.NetWork;
import com.beilong.chenzzs.app.AppConstant;
import com.beilong.chenzzs.bean.VersionAPI;
import com.beilong.chenzzs.ui.about.contract.AboutContract;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by LBL on 2016/11/7.
 */

public class AboutModel implements AboutContract.Model{
    @Override
    public Observable<VersionAPI> getVersionAPI() {
        return NetWork
                .getUtilsApi()
                .getVersionAPI(AppConstant.TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
