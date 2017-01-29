package com.android.movies.Background;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.android.movies.Data.MovieContract.MovieEntry;
import com.android.movies.JSONHandler.JsonHandler;
import com.android.movies.Model.Movie;
import com.android.movies.Network.NetworkRequestUtil;
import com.android.movies.R;

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
        ArrayList<Movie> movieArrayList = null;
        if (mSortBy.equals(getContext().getString(R.string.favs_key))) {
            movieArrayList = new ArrayList<>();
            Cursor cursor = getContext().getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String id, imageUrl, rating, name, synopsis, releaseDate, landscapePoster, trailerJson, reviewJson;
                id = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID));
                imageUrl = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_POSTER_URL));
                rating = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RATING));
                name = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_NAME));
                synopsis = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_SYNOPSIS));
                releaseDate = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
                landscapePoster = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_BACKDROP_URL));
                trailerJson = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TRAILER_JSON));
                reviewJson = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_REVIEW_JSON));
                movieArrayList.add(new Movie(id, imageUrl, rating, name, synopsis, releaseDate, landscapePoster, trailerJson, reviewJson));
            }
        } else {
            URL url = NetworkRequestUtil.buildUrlMoviesData(mSortBy, mPage);
            try {
                movieArrayList = JsonHandler.fetchJsonFromMovieData(NetworkRequestUtil.getResponseFromUrl(url));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return movieArrayList;
    }
}
