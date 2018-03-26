package dawidzior.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dawidzior.popularmovies.model.Movie;
import dawidzior.popularmovies.utils.NetworkUtils;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

    private final List<Movie> moviesList;
    private Context context;
    private MovieClickListener movieClickListener;

    public MoviesListAdapter(Context context, List<Movie> moviesList, MovieClickListener movieClickListener) {
        this.context = context;
        this.moviesList = moviesList;
        this.movieClickListener = movieClickListener;
    }

    @Override
    public MoviesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View poster = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_layout, parent, false);
        return new ViewHolder(poster);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesListAdapter.ViewHolder holder, int position) {
        //w500 to maintain image quality.
        Picasso.with(context).load(NetworkUtils.IMAGE_BASE_URL + "w500/" + moviesList.get(position).getPoster()).into
                (holder.posterView);

        holder.posterView.setTransitionName(String.valueOf(moviesList.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public interface MovieClickListener {
        void onClick(Movie movie, View sharedView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView posterView;

        ViewHolder(View itemView) {
            super(itemView);
            posterView = (ImageView) itemView;
            posterView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            movieClickListener.onClick(moviesList.get(getAdapterPosition()), view);
        }
    }
}
