package com.android.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.movies.Adapters.TrailerAdapter;
import com.android.movies.JSONHandler.JsonHandler;
import com.android.movies.Model.Movie;
import com.android.movies.Model.Review;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.OnTrailerClickListener {

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

    private Movie currentMovie;
    private ArrayList<String> trailerArrayList;
    private ArrayList<Review> reviewArrayList;
    private TrailerAdapter mTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Bundle dataPack = getIntent().getExtras();

        currentMovie = (Movie) dataPack.get("currentMovie");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(this).load(currentMovie.getmLandscapePosterUrl()).into(poster);
        Picasso.with(this).load(currentMovie.getmImageUrl()).into(iv_movie_poster);

        tv_title.setText(currentMovie.getmName());
        rating.setText(getString(R.string.rating_text_string, currentMovie.getmRating()));
        releaseDate.setText(currentMovie.getmReleaseDate());
        synopsis.setText(currentMovie.getmSynopsis());

        try {
            trailerArrayList = JsonHandler.getVideoLinksFromVideoData(currentMovie.getmTrailerUrlsJson());
            Log.v("trailerArraylist", currentMovie.getmTrailerUrlsJson());
            reviewArrayList = JsonHandler.getReviewsFromJson(currentMovie.getmReviewJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mTrailerAdapter = new TrailerAdapter(this, this, trailerArrayList);

        trailersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailersRecyclerView.setLayoutManager(linearLayoutManager);
        trailersRecyclerView.setAdapter(mTrailerAdapter);

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
    }

    @Override
    public void onClick(String key) {
        String trailerUrl = "http://www.youtube.com/watch?v=" + key;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl)));
    }
}
