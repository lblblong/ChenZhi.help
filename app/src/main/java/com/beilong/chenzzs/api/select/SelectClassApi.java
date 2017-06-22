package com.beilong.chenzzs.api.select;

import java.util.List;

import com.beilong.chenzzs.bean.Term;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by LBL on 2016/11/1.
 */

public interface SelectClassApi {
    @GET("main.json")
    Observable<List<Term>> getTerms();

    @GET
    Observable<ResponseBody> downloadSchoolTimeTable(@Url String fileUrl);
}
