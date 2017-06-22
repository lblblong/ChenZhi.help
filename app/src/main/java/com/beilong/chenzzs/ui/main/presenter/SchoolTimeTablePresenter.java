package com.beilong.chenzzs.ui.main.presenter;

import android.util.Log;

import com.beilong.chenzzs.app.AppConstant;
import com.beilong.chenzzs.bean.VersionAPI;
import com.beilong.chenzzs.ui.main.contract.SchoolTimeTableContract;
import com.beilong.chenzzs.utils.SharedPreferencesUtil;
import com.beilong.chenzzs.utils.Util;

import java.util.List;

import rx.Observer;

/**
 * Created by LBL on 2016/11/3.
 */

public class SchoolTimeTablePresenter extends SchoolTimeTableContract.Presenter {
    @Override
    public void getClassTable(String classname) {
        Observer observer = new Observer<List<String>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mView.returnClassTableOnError();
            }

            @Override
            public void onNext(List<String> strings) {
                mView.returnClassTable(strings);
            }
        };
        mModel.getClassTable(mContext.getFilesDir()+ AppConstant.SCHOOL_TIME_TABLE,classname).subscribe(observer);
    }

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
                String currentVersionName = SharedPreferencesUtil.getInstance().getCurrentVersion(Util.getVersion(mContext));
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
