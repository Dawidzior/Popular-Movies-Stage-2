package dawidzior.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dawidzior.popularmovies.model.Trailer;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.ViewHolder> {

    private static final String YOUTUBE_NAME = "YouTube";
    private static final String YOUTUBE_WATCH_LINK = "https://www.youtube.com/watch/?v=";
    private static final String YOUTUBE_THUMBNAIL_LINK = "https://img.youtube.com/vi/";
    private static final String YOUTUBE_THUMBNAIL_SUFFIX = "/hqdefault.jpg";
    private final List<Trailer> trailersList;
    private Context context;

    MovieTrailersAdapter(Context context, List<Trailer> trailersList) {
        this.context = context;
        this.trailersList = trailersList;
    }

    @Override
    public MovieTrailersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View poster = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item_layout, parent, false);
        return new MovieTrailersAdapter.ViewHolder(poster);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailersAdapter.ViewHolder holder, int position) {
        if (isYoutube(position)) {
            final String movieKey = trailersList.get(position).getKey();
            Picasso.with(context).load(YOUTUBE_THUMBNAIL_LINK + movieKey +
                    YOUTUBE_THUMBNAIL_SUFFIX).into(holder.trailerView);
            holder.trailerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(YOUTUBE_WATCH_LINK).buildUpon().appendPath(movieKey).build();
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    private boolean isYoutube(int position) {
        return trailersList.get(position).getSite().equals(YOUTUBE_NAME);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView trailerView;

        ViewHolder(View itemView) {
            super(itemView);
            trailerView = (ImageView) itemView;
        }
    }
}
