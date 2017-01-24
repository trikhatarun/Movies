package com.android.movies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by trikh on 14-01-2017.
 */

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String mId;
    private String mImageUrl;
    private String mRating;
    private String mName;
    private String mSynopsis;
    private String mReleaseDate;
    private String mTrailerUrlsJson;
    private String mReviewJson;
    private String mLandscapePosterUrl;

    public Movie(String id, String imageUrl, String rating, String name, String synopsis, String releaseDate, String landscapePosterUrl) {
        this.mId = id;
        this.mImageUrl = imageUrl;
        this.mRating = rating;
        this.mName = name;
        this.mSynopsis = synopsis;
        this.mReleaseDate = releaseDate;
        this.mLandscapePosterUrl = landscapePosterUrl;
    }

    protected Movie(Parcel in) {
        mId = in.readString();
        mImageUrl = in.readString();
        mRating = in.readString();
        mName = in.readString();
        mSynopsis = in.readString();
        mReleaseDate = in.readString();
        mLandscapePosterUrl = in.readString();
        mTrailerUrlsJson = in.readString();
        mReviewJson = in.readString();
    }

    public String getmId() {
        return mId;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmRating() {
        return mRating;
    }

    public String getmName() {
        return mName;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmTrailerUrlsJson() {
        return mTrailerUrlsJson;
    }

    public void setmTrailerUrlsJson(String mTrailerUrlsJson) {
        this.mTrailerUrlsJson = mTrailerUrlsJson;
    }

    public String getmReviewJson() {
        return mReviewJson;
    }

    public void setmReviewJson(String mReviewJson) {
        this.mReviewJson = mReviewJson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mImageUrl);
        parcel.writeString(mRating);
        parcel.writeString(mName);
        parcel.writeString(mSynopsis);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mLandscapePosterUrl);
        parcel.writeString(mTrailerUrlsJson);
        parcel.writeString(mReviewJson);

    }

    public String getmLandscapePosterUrl() {
        return mLandscapePosterUrl;
    }
}
