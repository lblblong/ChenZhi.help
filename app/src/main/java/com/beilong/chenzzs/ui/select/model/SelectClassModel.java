package com.beilong.chenzzs.ui.select.model;

import com.beilong.chenzzs.api.NetWork;
import com.beilong.chenzzs.app.AppConstant;
import com.beilong.chenzzs.bean.Term;
import com.beilong.chenzzs.ui.select.contract.SelectClassContract;
import com.beilong.chenzzs.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by LBL on 2016/11/1.
 */

public class SelectClassModel implements SelectClassContract.Model {

    @Override
    public Observable<List<Term>> getTerms(String Url) {
        return NetWork.getSelectClassApi(Url)
                .getTerms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResponseBody> downloadSchoolTimeTable(String Url) {
        return NetWork.getSelectClassApi("")
                .downloadSchoolTimeTable(Url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable saveSchoolTimeTable(final String path , final ResponseBody responseBody) {
        return Observable.create(new Observable.OnSubscribe<Observer>(){
            @Override
            public void call(Subscriber<? super Observer> subscriber) {
                File file = new File(path+ AppConstant.SCHOOL_TIME_TABLE);
                if(!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Reader reader = responseBody.charStream();
                try {
                    Writer writer = new FileWriter(file);
                    char[] c = new char[1024];
                    while (reader.read(c)!=-1){
                        writer.write(c);
                        writer.flush();
                    }
                    writer.close();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<String>> getClassList(final String path) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try {
                    StringBuffer sb = new StringBuffer();
                    try {
                        File file = new File(path);
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
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    JSONArray classnames = jsonObject.getJSONArray("classnames");
                    List<String> list = new ArrayList<String>();
                    int len = classnames.length();
                    for(int i=0 ; i<len ; i++){
                        list.add((String) classnames.get(i));
                    }
                    subscriber.onNext(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable saveCurrentClass(final String classname) {
        return Observable.create(new Observable.OnSubscribe<Observer>(){
            @Override
            public void call(Subscriber<? super Observer> subscriber) {
                SharedPreferencesUtil.getInstance().changeCurrentClass(classname);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
