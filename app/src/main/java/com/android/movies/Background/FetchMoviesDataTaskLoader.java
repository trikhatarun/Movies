package com.android.movies.Background;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.android.movies.JSONHandler.JsonHandler;
import com.android.movies.Model.Movie;
import com.android.movies.Network.NetworkRequestUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by trikh on 14-01-2017.
 */

public class FetchMoviesDataTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {
    private String mSortBy;
    private int mPage;

    public FetchMoviesDataTaskLoader(Context context, String sortBy, int page) {
        super(context);
        mSortBy = sortBy;
        mPage = page;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        URL url = NetworkRequestUtil.buildUrlMoviesData(mSortBy, mPage);
        ArrayList<Movie> movieArrayList;
        try {
            movieArrayList = JsonHandler.fetchJsonFromMovieData(NetworkRequestUtil.getResponseFromUrl(url));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return movieArrayList;
    }
}
