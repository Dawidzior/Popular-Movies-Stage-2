package dawidzior.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dawidzior.popularmovies.model.Review;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ViewHolder> {

    private final List<Review> reviewsList;

    MovieReviewsAdapter(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @Override
    public MovieReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View review = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout, parent, false);
        return new MovieReviewsAdapter.ViewHolder(review);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewsAdapter.ViewHolder holder, int position) {
        holder.author.setText(reviewsList.get(position).getAuthor());
        holder.review.setText(reviewsList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView author;
        TextView review;

        ViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            review = itemView.findViewById(R.id.review);
        }
    }

}
