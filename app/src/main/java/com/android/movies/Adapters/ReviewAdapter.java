package com.android.movies.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.movies.Model.Review;
import com.android.movies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trikh on 24-01-2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<Review> reviewsArrayList;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View review_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(review_view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.reviewAuthor.setText(reviewsArrayList.get(position).getmAuthor());
        holder.reviewContent.setText(reviewsArrayList.get(position).getmReviewContent());
    }

    @Override
    public int getItemCount() {
        if (reviewsArrayList == null) {
            return 0;
        } else
            return reviewsArrayList.size();
    }

    public void setReviewsArrayList(ArrayList<Review> reviewsList) {
        reviewsArrayList = reviewsList;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author)
        TextView reviewAuthor;
        @BindView(R.id.review_content)
        TextView reviewContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
