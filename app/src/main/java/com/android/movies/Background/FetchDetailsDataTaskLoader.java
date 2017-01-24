package com.android.movies.Background;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.movies.Network.NetworkRequestUtil;

import java.io.IOException;

/**
 * Created by trikh on 24-01-2017.
 */

public class FetchDetailsDataTaskLoader extends AsyncTaskLoader<Bundle> {
    private String Id;

    public FetchDetailsDataTaskLoader(Context context, String id) {
        super(context);
        Id = id;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Bundle loadInBackground() {
        Bundle bundle = new Bundle();
        String trailerJson = null, reviewJson = null;
        try {
            trailerJson = NetworkRequestUtil.getResponseFromUrl(NetworkRequestUtil.buildVideoURL(Id));
            reviewJson = NetworkRequestUtil.getResponseFromUrl(NetworkRequestUtil.buildReviewURL(Id));
            Log.v("reviewJson", reviewJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bundle.putString("trailerJson", trailerJson);
        bundle.putString("reviewJson", reviewJson);
        return bundle;
    }
}
