package com.beilong.chenzzs.api.utils;

import com.beilong.chenzzs.bean.VersionAPI;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by LBL on 2016/11/7.
 */

public interface UtilsApi {
    @GET("latest/581f4208ca87a8388c000899")
    Observable<VersionAPI> getVersionAPI(
            @Query("api_token")String api_token
    );
}
