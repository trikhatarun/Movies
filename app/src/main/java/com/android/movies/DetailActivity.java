package com.android.movies;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.movies.Adapters.ReviewAdapter;
import com.android.movies.Adapters.TrailerAdapter;
import com.android.movies.Background.FetchDetailsDataTaskLoader;
import com.android.movies.Data.MovieContract.MovieEntry;
import com.android.movies.JSONHandler.JsonHandler;
import com.android.movies.Model.Movie;
import com.android.movies.Model.Review;
import com.android.movies.Network.NetworkRequestUtil;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.OnTrailerClickListener, LoaderManager.LoaderCallbacks<Bundle> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.posterLandscape)
    ImageView poster;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.movie_name)
    TextView tv_title;
    @BindView(R.id.posterPortrait)
    ImageView iv_movie_poster;
    @BindView(R.id.rating_text)
    TextView rating;
    @BindView(R.id.release_date)
    TextView releaseDate;
    @BindView(R.id.synopsis)
    TextView synopsis;
    @BindView(R.id.rv_trailers)
    RecyclerView trailersRecyclerView;
    @BindView(R.id.rv_reviews)
    RecyclerView reviewsRecyclerView;
    @BindView(R.id.reviewLoadingBar)
    ProgressBar reviewLoadingBar;
    @BindView(R.id.trailerLoadingBar)
    ProgressBar trailerLoadingBar;
    @BindView(R.id.likeButton)
    LikeButton likeButton;

    private Movie currentMovie;
    private ArrayList<String> trailerArrayList;
    private ArrayList<Review> reviewArrayList;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private SharedPreferences preferences;
    private Set<String> favsIdSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Bundle dataPack = getIntent().getExtras();

        currentMovie = (Movie) dataPack.get("currentMovie");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        favsIdSet = preferences.getStringSet(getString(R.string.cache_set_key), new HashSet<String>());
        Log.v("facIdser: ", favsIdSet.toString());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(this).load(currentMovie.getmLandscapePosterUrl()).into(poster);
        Picasso.with(this).load(currentMovie.getmImageUrl()).into(iv_movie_poster);

        tv_title.setText(currentMovie.getmName());
        rating.setText(getString(R.string.rating_text_string, currentMovie.getmRating()));
        releaseDate.setText(currentMovie.getmReleaseDate());
        synopsis.setText(currentMovie.getmSynopsis());

        mTrailerAdapter = new TrailerAdapter(this, this);
        mReviewAdapter = new ReviewAdapter();

        if (favsIdSet.contains(currentMovie.getmId())) {
            likeButton.setLiked(true);
            String projection[] = {
                    MovieEntry.COLUMN_REVIEW_JSON,
                    MovieEntry.COLUMN_TRAILER_JSON
            };
            Cursor c = getContentResolver().query(ContentUris.withAppendedId(MovieEntry.CONTENT_URI, 1), projection, MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{currentMovie.getmId()}, null);
            if (c != null) {
                try {
                    c.moveToFirst();
                    trailerArrayList = JsonHandler.getVideoLinksFromVideoData(c.getString(c.getColumnIndex(MovieEntry.COLUMN_TRAILER_JSON)));
                    reviewArrayList = JsonHandler.getReviewsFromJson(c.getString(c.getColumnIndex(MovieEntry.COLUMN_REVIEW_JSON)));
                    showRecyclerViews();
                    if (!reviewArrayList.isEmpty() || !(reviewArrayList == null)) {
                        mReviewAdapter.setReviewsArrayList(reviewArrayList);
                    }
                    if (!trailerArrayList.isEmpty() || !(trailerArrayList == null))
                        mTrailerAdapter.setTrailerKeyList(trailerArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                c.close();
            }
        } else {
            loadData();
        }

        trailersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailersRecyclerView.setLayoutManager(linearLayoutManager);
        trailersRecyclerView.setAdapter(mTrailerAdapter);

        reviewsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(linearLayoutManager1);
        reviewsRecyclerView.setAdapter(mReviewAdapter);

        //To show name only when bar is collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(currentMovie.getmName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ContentValues values = new ContentValues();
                values.put(MovieEntry.COLUMN_MOVIE_ID, currentMovie.getmId());
                values.put(MovieEntry.COLUMN_MOVIE_NAME, currentMovie.getmName());
                values.put(MovieEntry.COLUMN_RATING, currentMovie.getmRating());
                values.put(MovieEntry.COLUMN_RELEASE_DATE, currentMovie.getmReleaseDate());
                values.put(MovieEntry.COLUMN_MOVIE_POSTER_URL, currentMovie.getmImageUrl());
                values.put(MovieEntry.COLUMN_MOVIE_BACKDROP_URL, currentMovie.getmLandscapePosterUrl());
                values.put(MovieEntry.COLUMN_SYNOPSIS, currentMovie.getmSynopsis());
                values.put(MovieEntry.COLUMN_REVIEW_JSON, currentMovie.getmReviewJson());
                values.put(MovieEntry.COLUMN_TRAILER_JSON, currentMovie.getmTrailerUrlsJson());

                Uri uri = getContentResolver().insert(MovieEntry.CONTENT_URI, values);
                if (uri == null) {
                    Toast.makeText(DetailActivity.this, "Failed to add to Favourites", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                    favsIdSet.add(currentMovie.getmId());
                    updatePreference();
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int deleteResponse = getContentResolver().delete(MovieEntry.CONTENT_URI, MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{currentMovie.getmId()});
                if (deleteResponse > 0) {
                    Toast.makeText(DetailActivity.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                    favsIdSet.remove(currentMovie.getmId());
                    updatePreference();
                } else {
                    Toast.makeText(DetailActivity.this, "Failed to remove from favourites", Toast.LENGTH_SHORT).show();
                }
                favsIdSet.remove(currentMovie.getmId());
            }
        });
    }

    private void updatePreference() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(getString(R.string.cache_set_key), favsIdSet);
        editor.apply();
    }

    private void loadData() {
        if (NetworkRequestUtil.isNetworkAvailable(this)) {
            LoaderManager loaderManager = getLoaderManager();
            int LOADER_ID = 2240;
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRecyclerViews() {
        trailersRecyclerView.setVisibility(View.VISIBLE);
        reviewsRecyclerView.setVisibility(View.VISIBLE);
        trailerLoadingBar.setVisibility(View.GONE);
        reviewLoadingBar.setVisibility(View.GONE);
        likeButton.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_share) {
            Intent shareIntent =
                    new Intent(android.content.Intent.ACTION_SEND);
            String trailerUrl = "http://www.youtube.com/watch?v=" + trailerArrayList.get(0);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, trailerUrl);
            startActivity(Intent.createChooser(shareIntent,
                    "Share Trailer"));

        }
        return true;
    }

    @Override
    public void onClick(String key) {
        String trailerUrl = "http://www.youtube.com/watch?v=" + key;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl)));
    }

    @Override
    public Loader<Bundle> onCreateLoader(int id, Bundle args) {
        return new FetchDetailsDataTaskLoader(this, currentMovie.getmId());
    }

    @Override
    public void onLoadFinished(Loader<Bundle> loader, Bundle data) {
        if (data != null) {
            String trailerJson = data.getString("trailerJson");
            String reviewJson = data.getString("reviewJson");
            currentMovie.setmTrailerUrlsJson(trailerJson);
            try {
                trailerArrayList = JsonHandler.getVideoLinksFromVideoData(trailerJson);
                reviewArrayList = JsonHandler.getReviewsFromJson(reviewJson);
                if (reviewJson == null) {
                    currentMovie.setmReviewJson(null);
                } else
                    currentMovie.setmReviewJson(reviewJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        showRecyclerViews();
        mReviewAdapter.setReviewsArrayList(reviewArrayList);
        mTrailerAdapter.setTrailerKeyList(trailerArrayList);
    }

    @Override
    public void onLoaderReset(Loader<Bundle> loader) {
        mReviewAdapter.setReviewsArrayList(null);
        mTrailerAdapter.setTrailerKeyList(null);
    }
}
