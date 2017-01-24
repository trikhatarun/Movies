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

    public Review(String Author, String ReviewBody) {
        this.mAuthor = Author;
        this.mReviewContent = ReviewBody;
    }

    protected Review(Parcel in) {
        mAuthor = in.readString();
        mReviewContent = in.readString();
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmReviewContent() {
        return mReviewContent;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAuthor);
        parcel.writeString(mReviewContent);
    }
}
