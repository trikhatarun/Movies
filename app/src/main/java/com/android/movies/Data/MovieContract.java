package com.android.movies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by trikh on 25-01-2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.android.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "favourites";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "favourites";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_NAME = "movie_name";

        public static final String COLUMN_MOVIE_POSTER_URL = "poster_url";

        public static final String COLUMN_MOVIE_BACKDROP_URL = "backdrop_url";

        public static final String COLUMN_TRAILER_JSON = "trailer_json";

        public static final String COLUMN_REVIEW_JSON = "review_json";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_SYNOPSIS = "synopsis";

    }
}
