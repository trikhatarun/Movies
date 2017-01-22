package com.android.movies.Background;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.android.movies.Network.NetworkRequestUtil;

import java.io.IOException;
import java.net.URL;

/**
 * Created by trikh on 21-01-2017.
 */

public class FetchDetailsDataTaskLoader extends AsyncTaskLoader<Bundle> {

    private String mId;

    public FetchDetailsDataTaskLoader(Context context, String id) {
        super(context);
        mId = id;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Bundle loadInBackground() {
        Bundle bundle = new Bundle();

        URL reviewUrl, trailerUrl;
        trailerUrl = NetworkRequestUtil.buildVideoURL(mId);
        reviewUrl = NetworkRequestUtil.buildReviewURL(mId);

        String trailerJson = null, reviewJson = null;
        try {
            trailerJson = NetworkRequestUtil.getResponseFromUrl(trailerUrl);
            reviewJson = NetworkRequestUtil.getResponseFromUrl(reviewUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bundle.putString("trailerJson", trailerJson);
        bundle.putString("reviewJson", reviewJson);
        return bundle;
    }
}
