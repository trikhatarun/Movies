package com.android.movies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.movies.Data.MovieContract.MovieEntry;

/**
 * Created by trikh on 25-01-2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " +
                MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_POSTER_URL + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_BACKDROP_URL + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TRAILER_JSON + " TEXT, " +
                MovieEntry.COLUMN_REVIEW_JSON + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
