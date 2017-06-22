package com.beilong.chenzzs.api;

import com.beilong.chenzzs.api.select.SelectClassApi;
import com.beilong.chenzzs.api.utils.UtilsApi;
import com.beilong.chenzzs.app.AppConstant;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LBL on 2016/11/1.
 */

public class NetWork {
    private static SelectClassApi selectClassApi;
    private static UtilsApi utilsApi;

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static SelectClassApi getSelectClassApi(String url) {
        if (selectClassApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(url)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            selectClassApi = retrofit.create(SelectClassApi.class);
        }
        return selectClassApi;
    }

    public static UtilsApi getUtilsApi(){
        if (utilsApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(AppConstant.UTIL_BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            utilsApi = retrofit.create(UtilsApi.class);
        }
        return utilsApi;
    }
}
