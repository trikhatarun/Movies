package com.android.movies.JSONHandler;

import android.util.Log;

import com.android.movies.Model.Movie;
import com.android.movies.Model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by trikh on 14-01-2017.
 */

public class JsonHandler {
    //Function to extract movies list from the json data
    public static ArrayList<Movie> fetchJsonFromMovieData(String stringResponse) throws JSONException {
        final String imagePrefixUrl = "https://image.tmdb.org/t/p/w500";
        ArrayList<Movie> data = new ArrayList<>();

        JSONObject jsonResponse = new JSONObject(stringResponse);
        JSONArray result = jsonResponse.getJSONArray("results");

        for (int i = 0; i < result.length(); i++) {
            JSONObject movieJson = result.getJSONObject(i);

            //getting movie id
            String id = movieJson.getString("id");

            //getting poster link
            String imageUrl = imagePrefixUrl + movieJson.getString("poster_path").substring(0);

            //getting landscape poster
            String landscapePoster = imagePrefixUrl + movieJson.getString("backdrop_path").substring(0);

            //getting movie title
            String title = movieJson.getString("original_title");

            //getting synopsis
            String synopsis = movieJson.getString("overview");

            //getting rating
            String rating = movieJson.getString("vote_average");

            //getting release date
            String releaseDate = movieJson.getString("release_date");

            data.add(new Movie(id, imageUrl, rating, title, synopsis, releaseDate, landscapePoster));
        }
        return data;
    }

    //Function to fetch videos links list from the json data
    public static ArrayList<String> getVideoLinksFromVideoData(String stringResponse) throws JSONException {
        ArrayList<String> vidsList = new ArrayList<>();

        JSONObject jsonResponse = new JSONObject(stringResponse);
        JSONArray result = jsonResponse.getJSONArray("results");

        for (int i = 0; i < result.length(); i++) {
            JSONObject vidJson = result.getJSONObject(i);
            String key = vidJson.getString("key");
            if (key == null) {
                Log.v("Error at " + i, stringResponse);
            }
            vidsList.add(key);
        }
        return vidsList;
    }

    //Function to fetch reviews list from the json data
    public static ArrayList<Review> getReviewsFromJson(String stringResponse) throws JSONException {
        ArrayList<Review> reviewArrayList = new ArrayList<>();

        JSONObject jsonResponse = new JSONObject(stringResponse);
        JSONArray result = jsonResponse.getJSONArray("results");

        for (int i = 0; i < result.length(); i++) {
            JSONObject reviewJson = result.getJSONObject(i);

            String author = reviewJson.getString("author");
            String content = reviewJson.getString("content");

            reviewArrayList.add(new Review(author, content));
        }
        return reviewArrayList;
    }
}
