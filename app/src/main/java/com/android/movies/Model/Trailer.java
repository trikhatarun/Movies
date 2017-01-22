package com.android.movies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model to store Trailer details for a particular moviw
 */

public class Trailer implements Parcelable {
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    private String trailerTitle;
    private String trailerKey;
    private String trailerSite;

    public Trailer(String title, String key, String site) {
        trailerTitle = title;
        trailerKey = key;
        trailerSite = site;
    }

    protected Trailer(Parcel in) {
        trailerTitle = in.readString();
        trailerKey = in.readString();
        trailerSite = in.readString();
    }

    public String getTrailerTitle() {
        return trailerTitle;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public String getTrailerSite() {
        return trailerSite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(trailerTitle);
        parcel.writeString(trailerKey);
        parcel.writeString(trailerSite);
    }
}
