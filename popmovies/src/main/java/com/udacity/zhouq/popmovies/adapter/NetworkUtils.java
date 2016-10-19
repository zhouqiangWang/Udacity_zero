package com.udacity.zhouq.popmovies.adapter;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.udacity.zhouq.popmovies.R;

/**
 * Created by zhouqiang.wang on 16/10/16.
 */

public class NetworkUtils {
  public static final String TMDB_API_PREFIX = "https://api.themoviedb.org/3/movie/";
  public static final char PARAMETER_PREFIX = '?';
  public static final char PARAMETER_EQUAL = '=';
  public static final char PARAMETER_AND = '&';
  public static final String PARA_LANGUAGE = "language";
  public static final String PARA_API_KEY = "api_key";
  public static final String PARA_PAGE = "page";
  public static final String TMDB_IMAGE_PREFIX = "http://image.tmdb.org/t/p/w185/";
  public static final String TMDB_CATEGORY_POP = "popular";
  public static final String TMDB_CATEGORY_TOP_RATED = "top_rated";
  public static final String TMDB_LANGUAGE_ZH = "zh";

  private final String TMDB_API_KEY;
  private final RequestQueue mVolleyQueue;
  private NetworkUtils(Context context){
    TMDB_API_KEY = context.getString(R.string.tmdb_api_key);
    mVolleyQueue = Volley.newRequestQueue(context);

  }

  private static NetworkUtils mInstance;
  synchronized public static NetworkUtils getInstance(Context context){
    if(mInstance == null){
      mInstance = new NetworkUtils(context);
    }

    return mInstance;
  }
  public String buildURLWithLan(String catagory, String language){
    return buildURLWithPage(catagory, language, 1);
  }

  public String buildURLWithPage(String catagory, String language, int page){
    StringBuilder urlBuilder = new StringBuilder(NetworkUtils.TMDB_API_PREFIX);
    String url = urlBuilder.append(catagory).append(PARAMETER_PREFIX)
        .append(PARA_LANGUAGE).append(PARAMETER_EQUAL).append(language)
        .append(PARAMETER_AND).append(PARA_API_KEY).append(PARAMETER_EQUAL)
        .append(TMDB_API_KEY).append(PARAMETER_AND).append(PARA_PAGE)
        .append(PARAMETER_EQUAL).append(page).toString();

    return url;
  }

  public String buildURLWithMovieID(String id){
    StringBuilder urlBuilder = new StringBuilder(NetworkUtils.TMDB_API_PREFIX);
    String url = urlBuilder.append(id).append(PARAMETER_PREFIX)
        .append(PARA_LANGUAGE).append(PARAMETER_EQUAL).append(TMDB_LANGUAGE_ZH)
        .append(PARAMETER_AND).append(PARA_API_KEY).append(PARAMETER_EQUAL)
        .append(TMDB_API_KEY).append(PARAMETER_AND).append(PARA_PAGE).toString();

    return url;
  }

  public RequestQueue getVolleyQueue(){
    return mVolleyQueue;
  }
}
