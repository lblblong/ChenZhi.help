package com.beilong.chenzzs.ui.main.model;

import com.beilong.chenzzs.api.NetWork;
import com.beilong.chenzzs.app.AppConstant;
import com.beilong.chenzzs.bean.VersionAPI;
import com.beilong.chenzzs.ui.main.contract.SchoolTimeTableContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by LBL on 2016/11/3.
 */

public class SchoolTimeTableModel implements SchoolTimeTableContract.Model {
    @Override
    public Observable<List<String>> getClassTable(final String path, final String classname) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                File file = new File(path);
                StringBuffer sb = new StringBuffer();
                try {
                    Reader reader = new FileReader(file);
                    char[] c = new char[1024];
                    while (reader.read(c)!=-1){
                        sb.append(c);
                    }
                    reader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject root = new JSONObject(sb.toString());    //顶级节点
                    JSONObject classtable = root.getJSONObject(classname);  //班级节点
                    List<String> courses = new ArrayList<String>();
                    for(int i = 1;i<=20;i++){   //固定课程节数20节
                        String coursename = classtable.getString(i+"");
                        if(coursename.isEmpty()){
                            courses.add(i-1,"");
                        }else {
                            courses.add(i-1,coursename);
                        }
                    }
                    subscriber.onNext(courses);
                    subscriber.onCompleted();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<VersionAPI> getVersionAPI() {
        return NetWork.getUtilsApi()
                .getVersionAPI(AppConstant.TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
