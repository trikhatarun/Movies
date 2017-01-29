package com.android.movies.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.android.movies.Data.MovieContract.MovieEntry;

/**
 * Created by trikh on 25-01-2017.
 */

public class MovieContentProvider extends ContentProvider {

    public static final int FAV_MOVIES = 101;
    public static final int FAV_MOVIE_WITH_ID = 102;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, FAV_MOVIES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", FAV_MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortBy) {
        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case FAV_MOVIE_WITH_ID:
                cursor = db.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortBy);
                break;
            case FAV_MOVIES:
                cursor = db.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortBy);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;
        switch (match) {
            case FAV_MOVIES:
                long id = db.insert(MovieEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
                } else {
                    return null;
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int returnInt;
        returnInt = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
        return returnInt;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }
}
