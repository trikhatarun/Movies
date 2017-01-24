package com.android.movies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trikh on 23-01-2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private final OnTrailerClickListener playTrailerListener;
    private ArrayList<String> TrailerKeyList;
    private Context mContext;

    public TrailerAdapter(Context context, OnTrailerClickListener playTrailerListener, ArrayList<String> trailerKeyList) {
        Log.v("Adapter: ", "Adapter initiated");
        this.playTrailerListener = playTrailerListener;
        TrailerKeyList = trailerKeyList;
        mContext = context;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v("Adapter", "Adapter working");
        View trailer_list_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(trailer_list_item);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Picasso.with(mContext).load("http://img.youtube.com/vi/" + TrailerKeyList.get(position) + "/hqdefault.jpg").into(holder.trailerThumbnail);
    }

    @Override
    public int getItemCount() {
        if (TrailerKeyList == null)
            return 0;
        return TrailerKeyList.size();
    }

    public interface OnTrailerClickListener {
        void onClick(String key);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.trailer_thumbnail)
        ImageView trailerThumbnail;
        @BindView(R.id.trailer_play)
        ImageButton playButton;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            playButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            playTrailerListener.onClick(TrailerKeyList.get(getAdapterPosition()));
        }
    }
}
