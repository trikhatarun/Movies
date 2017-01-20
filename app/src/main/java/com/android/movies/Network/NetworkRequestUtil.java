package com.android.movies.Network;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by trikh on 13-01-2017.
 */

public class NetworkRequestUtil {

    private static final String TAG = NetworkRequestUtil.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String API_KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";
    private static final String VOTES_PATH = "top_rated";
    private static final String POPULARITY_PATH = "popular";
    private static final String LANGUAGE = "language";
    private static final String VIDEOS = "videos";
    private static final String REVIEWS = "reviews";

    private static final String key = "97f469b9e89b30f1f7d07e8b35973e56";
    private static final String language = "en-US";

    //Function used to build URL for fetching movies
    public static URL buildUrlMoviesData(String sortBy, int page) {
        Uri.Builder tempUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, key)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(page))
                .appendQueryParameter(LANGUAGE, language);

        Uri builtUri;
        URL url = null;
        if (sortBy.equals("votes")) {
            builtUri = tempUri.appendPath(VOTES_PATH).build();
        } else
            builtUri = tempUri.appendPath(POPULARITY_PATH).build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Built Url: " + url);
        return url;
    }

    //Function to build URL for fetching Videos
    public static URL buildVideoURL(String id) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(VIDEOS).build();
        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Function to build URL for fetching reviews
    public static URL buildReviewURL(String id) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(REVIEWS).build();
        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Function used to fetch data from any URL provided
    public static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
