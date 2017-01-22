package com.android.movies;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.android.movies.Background.FetchDetailsDataTaskLoader;
import com.android.movies.JSONHandler.JsonHandler;
import com.android.movies.Model.Movie;
import com.android.movies.Model.Review;
import com.android.movies.Model.Trailer;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.poster)
    ImageView poster;

    private Movie currentMovie;
    private ArrayList<Trailer> trailerArrayList;
    private ArrayList<Review> reviewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int LOADER_ID = 2240;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Bundle dataPack = getIntent().getExtras();

        currentMovie = (Movie) dataPack.get("currentMovie");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar.setTitle("");
        Picasso.with(this).load(currentMovie.getmLandscapePosterUrl()).into(poster);

        if (currentMovie.getmTrailerUrlsJson() == null || currentMovie.getmReviewJson() == null) {
            LoaderManager loaderManager = getLoaderManager();
            Loader<ArrayList<Movie>> listLoader = loaderManager.getLoader(LOADER_ID);
            if (listLoader != null) {
                loaderManager.restartLoader(LOADER_ID, null, this);
            } else {
                loaderManager.initLoader(LOADER_ID, null, this);
            }
        } else {
            try {
                trailerArrayList = JsonHandler.getVideoLinksFromVideoData(currentMovie.getmTrailerUrlsJson());
                reviewArrayList = JsonHandler.getReviewsFromJson(currentMovie.getmReviewJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Loader<Bundle> onCreateLoader(int id, Bundle args) {
        return new FetchDetailsDataTaskLoader(this, currentMovie.getmId());
    }

    @Override
    public void onLoadFinished(Loader<Bundle> loader, Bundle data) {
        String trailerJson = data.getString("trailerJson");
        String reviewJson = data.getString("reviewJson");

        currentMovie.setmTrailerUrlsJson(trailerJson);
        currentMovie.setmReviewJson(reviewJson);

        try {
            trailerArrayList = JsonHandler.getVideoLinksFromVideoData(currentMovie.getmTrailerUrlsJson());
            reviewArrayList = JsonHandler.getReviewsFromJson(currentMovie.getmReviewJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Bundle> loader) {
    }
}
