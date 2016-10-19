package com.udacity.zhouq.popmovies.adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouqiang.wang on 16/10/17.
 */

public class MovieItem implements Parcelable{
  public String poster;
  public String overview;
  public String id;
  public String title;
  public String releaseDate;
  public String voteRates;

  public MovieItem(){
  }

  protected MovieItem(Parcel in) {
    poster = in.readString();
    overview = in.readString();
    id = in.readString();
    title = in.readString();
    releaseDate = in.readString();
    voteRates = in.readString();
  }

  public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
    @Override public MovieItem createFromParcel(Parcel in) {
      return new MovieItem(in);
    }

    @Override public MovieItem[] newArray(int size) {
      return new MovieItem[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(poster);
    parcel.writeString(overview);
    parcel.writeString(id);
    parcel.writeString(title);
    parcel.writeString(releaseDate);
    parcel.writeString(voteRates);
  }
}
