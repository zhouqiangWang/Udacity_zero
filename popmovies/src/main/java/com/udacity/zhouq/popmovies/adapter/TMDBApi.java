package com.udacity.zhouq.popmovies.adapter;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by zhouqiang.wang on 16/10/17.
 */
//TODO: retrofit2 doesnot work. I will try it in next project..
@Deprecated
public interface TMDBApi {
  @GET("3/movie/{catagory}")
  Call<List<String>> listMoviesWithPage(@Path("catagory") String catagory, @QueryMap Map<String, String> paramaters);
}
