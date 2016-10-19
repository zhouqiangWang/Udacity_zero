package com.udacity.zhouq.popmovies;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by zhouqiang.wang on 16/10/16.
 */

public class MovieApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();

    Fresco.initialize(this);
  }
}
