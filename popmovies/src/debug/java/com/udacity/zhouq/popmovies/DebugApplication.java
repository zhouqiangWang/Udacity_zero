package com.udacity.zhouq.popmovies;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

/**
 * Created by zhouqiang.wang on 16/10/16.
 */

public class DebugApplication extends Application {
  private static final String TAG = DebugApplication.class.getSimpleName();

  @Override public void onCreate() {
    super.onCreate();

    long startTime = SystemClock.elapsedRealtime();
    // init start
    Stetho.initializeWithDefaults(this);
    Fresco.initialize(this);


    long elapsed = SystemClock.elapsedRealtime() - startTime;

    Log.d(TAG, "onCreate: initialization done. It takes  " + elapsed + " ms");
  }

}
