package com.android.movies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.movies.Adapters.MovieAdapter;
import com.android.movies.Background.FetchMoviesDataTaskLoader;
import com.android.movies.Model.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>, MovieAdapter.OnMovieClickListener {

    private final int LOADER_ID = 1140;
    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_internet_view)
    LinearLayout noInternetView;
    @BindView(R.id.no_favs_view)
    LinearLayout noFavsView;
    @BindView(R.id.loadingBar)
    ProgressBar loadingbar;
    private String sort_by;
    private MovieAdapter mMovieAdapter;
    private Boolean sort_popularity, sort_votes;
    private MenuItem popButton;
    private MenuItem votButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadPreferences();

        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.filter_menu, menu);
        popButton = menu.findItem(R.id.popularity_button);
        votButton = menu.findItem(R.id.votes_button);
        if (sort_popularity) {
            popButton.setChecked(true);
            votButton.setChecked(false);
        } else {
            votButton.setChecked(true);
            popButton.setChecked(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        if (item.getItemId() == R.id.popularity_button) {
            if (!sort_popularity) {
                popButton.setChecked(true);
                votButton.setChecked(false);
                editor.putBoolean(getString(R.string.popularity_key), true);
                editor.putBoolean(getString(R.string.votes_key), false);
                editor.apply();
                loadPreferences();
                loadData();
            }
        } else {
            if (!sort_votes) {
                votButton.setChecked(true);
                popButton.setChecked(false);
                editor.putBoolean(getString(R.string.popularity_key), false);
                editor.putBoolean(getString(R.string.votes_key), true);
                editor.apply();
                loadPreferences();
                loadData();
            }
        }
        return true;
    }

    //Function to check internet availability, so that we do not start processing if internet is not available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Function to show view if there is an internet error
    private void showInternetError() {
        loadingbar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        noFavsView.setVisibility(View.GONE);
        noInternetView.setVisibility(View.VISIBLE);
    }

    //Function to show view in case of empty favourites list
    private void showNoFavsView() {
        loadingbar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        noInternetView.setVisibility(View.GONE);
        noFavsView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        loadingbar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        noInternetView.setVisibility(View.GONE);
        noFavsView.setVisibility(View.GONE);
    }

    //Load preferences
    private void loadPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sort_popularity = preferences.getBoolean(getString(R.string.popularity_key), true);
        sort_votes = preferences.getBoolean(getString(R.string.votes_key), false);
        if (sort_popularity) {
            sort_by = getString(R.string.popularity_key);
        } else
            sort_by = getString(R.string.votes_key);
        setLabel();
    }

    //Function to set appropriate lable
    public void setLabel() {
        if (sort_popularity) {
            setTitle(getString(R.string.menu_popularity));
        } else
            setTitle(getString(R.string.menu_votes));
    }

    //Function to load data and start loader
    public void loadData() {
        if (sort_popularity || sort_votes) {
            if (isNetworkAvailable()) {
                LoaderManager loaderManager = getLoaderManager();
                Loader<ArrayList<Movie>> listLoader = loaderManager.getLoader(LOADER_ID);
                if (listLoader != null) {
                    loaderManager.restartLoader(LOADER_ID, null, this);
                } else {
                    loaderManager.initLoader(LOADER_ID, null, this);
                }
            } else {
                showInternetError();
            }
        } else {

        }
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        loadingbar.setVisibility(View.VISIBLE);

        return new FetchMoviesDataTaskLoader(this, sort_by, 1);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        if (data != null) {
            showRecyclerView();
            mMovieAdapter.setMovieList(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        mMovieAdapter.setMovieList(null);
    }

    @Override
    public void onClick(Movie currentMovie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("currentMovie", currentMovie);
        startActivity(intent);
    }
}
