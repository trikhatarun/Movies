package com.android.movies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.movies.Model.Movie;
import com.android.movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trikh on 14-01-2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final OnMovieClickListener clickListener;
    private ArrayList<Movie> MovieList;
    private Context mContext;

    public MovieAdapter(Context context, OnMovieClickListener listener) {
        mContext = context;
        clickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View list_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        return new MovieViewHolder(list_item);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Picasso.with(mContext).load(MovieList.get(position).getmImageUrl()).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        if (MovieList == null)
            return 0;
        return MovieList.size();
    }

    public void setMovieList(ArrayList<Movie> movieList) {
        MovieList = null;
        MovieList = movieList;
        notifyDataSetChanged();
    }

    public interface OnMovieClickListener {
        void onClick(Movie currentMovie);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_poster)
        ImageView moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(MovieList.get(getAdapterPosition()));
        }
    }
}
