package com.android.movies.Model;

/**
 * Created by trikh on 14-01-2017.
 */

public class Review {
    private String mAuthor;
    private String mReviewContent;
    private String mUrl;

    public Review(String Author, String ReviewBody, String url) {
        this.mAuthor = Author;
        this.mReviewContent = ReviewBody;
        mUrl = url;
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
}
