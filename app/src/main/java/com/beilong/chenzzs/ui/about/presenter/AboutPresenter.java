package com.beilong.chenzzs.ui.about.presenter;

import android.util.Log;

import com.beilong.chenzzs.bean.VersionAPI;
import com.beilong.chenzzs.ui.about.contract.AboutContract;
import com.beilong.chenzzs.utils.Util;

import rx.Observer;

/**
 * Created by LBL on 2016/11/7.
 */

public class AboutPresenter extends AboutContract.Presenter{
    @Override
    public void checkVersion() {
        Observer<VersionAPI> observer = new Observer<VersionAPI>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(VersionAPI versionAPI) {
                Log.e("AboutPre",versionAPI.toString());
                String firVersionName = versionAPI.getVersionShort();
                String currentVersionName = Util.getVersion(mContext);
                if (currentVersionName.compareTo(firVersionName)<0){
                    mView.returnCheckVersion(versionAPI);   //需要更新
                }else{
                    mView.returnCheckVersion(null);     //无需更新
                }
            }
        };
        mModel.getVersionAPI().subscribe(observer);
    }
}
