package com.android.movies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by trikh on 14-01-2017.
 */

public class Review implements Parcelable {
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    private String mAuthor;
    private String mReviewContent;
    private String mUrl;

    public Review(String Author, String ReviewBody, String url) {
        this.mAuthor = Author;
        this.mReviewContent = ReviewBody;
        mUrl = url;
    }

    protected Review(Parcel in) {
        mAuthor = in.readString();
        mReviewContent = in.readString();
        mUrl = in.readString();
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmReviewContent() {
        return mReviewContent;
    }

    public String getmUrl() {
        return mUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAuthor);
        parcel.writeString(mReviewContent);
        parcel.writeString(mUrl);
    }
}
