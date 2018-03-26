package dawidzior.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dawidzior.popularmovies.model.Movie;
import dawidzior.popularmovies.model.Review;
import dawidzior.popularmovies.model.Trailer;
import dawidzior.popularmovies.utils.JsonParser;
import dawidzior.popularmovies.utils.NetworkUtils;

import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_MOVIE_ID;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_ORIGINAL_TITLE;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_OVERVIEW;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_POSTER_PATH;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_RELEASE_DATE;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_REVIEWS_JSON;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_TITLE;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_TRAILERS_JSON;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_USER_RATING;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.CONTENT_URI;

public class DetailsActivity extends AppCompatActivity {

    public static final String MOVIE_BUNDLE_KEY = "MOVIE_BUNDLE_KEY";
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.details_poster_view)
    protected ImageView detailsPoster;
    @BindView(R.id.details_original_title)
    protected TextView originalTitle;
    @BindView(R.id.details_rating_bar)
    protected RatingBar ratingBar;
    @BindView(R.id.details_release_date)
    protected TextView releaseDate;
    @BindView(R.id.favourite_button)
    protected ToggleButton favouriteButton;
    @BindView(R.id.overview)
    protected TextView overview;
    @BindView(R.id.trailers_label)
    protected TextView trailersLabel;
    @BindView(R.id.trailers_list)
    protected RecyclerView trailersList;
    @BindView(R.id.reviews_label)
    protected TextView reviewsLabel;
    @BindView(R.id.reviews_list)
    protected RecyclerView reviewsList;
    private Movie movie;
    private ScaleAnimation scaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: Fix shared element transition blinking.

        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            movie = getIntent().getParcelableExtra(MOVIE_BUNDLE_KEY);
        } else {
            movie = savedInstanceState.getParcelable(MOVIE_BUNDLE_KEY);
        }

        setTitle(movie.getTitle());

        detailsPoster.setTransitionName(MainActivity.SHARED_ELEMENT);

        //w500 to maintain image quality.
        Picasso.with(this).load(NetworkUtils.IMAGE_BASE_URL + "w500/" + movie.getPoster()).into(detailsPoster);
        originalTitle.setText(movie.getOriginalTitle());
        ratingBar.setRating(Float.valueOf(movie.getUserRating().toString()));
        releaseDate.setText(movie.getReleaseDate());
        overview.setText(movie.getOverview());
        favouriteButton.setChecked(isMovieFavoured());

        scaleAnimation =
                new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        favouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!isMovieFavoured()) {
                    addMovieToFavourites();
                    Toast.makeText(DetailsActivity.this, R.string.added_to_favourites, Toast.LENGTH_SHORT).show();
                } else {
                    deleteMovieFromFavourites();
                    Toast.makeText(DetailsActivity.this, R.string.deleted_from_favourites, Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpTrailers(movie.getTrailersJson());
        setUpReviews(movie.getReviewsJson());
    }

    private void addMovieToFavourites() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(COLUMN_TITLE, movie.getTitle());
        contentValues.put(COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(COLUMN_POSTER_PATH, movie.getPoster());
        contentValues.put(COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(COLUMN_USER_RATING, movie.getUserRating());
        contentValues.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(COLUMN_TRAILERS_JSON, movie.getTrailersJson());
        contentValues.put(COLUMN_REVIEWS_JSON, movie.getReviewsJson());

        getContentResolver().insert(CONTENT_URI, contentValues);
        favouriteButton.startAnimation(scaleAnimation);
        favouriteButton.setChecked(true);
    }

    private void deleteMovieFromFavourites() {
        Uri uri = CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build();
        getContentResolver().delete(uri, null, null);
        getContentResolver().notifyChange(uri, null);
        favouriteButton.startAnimation(scaleAnimation);
        favouriteButton.setChecked(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_BUNDLE_KEY, movie);
        super.onSaveInstanceState(outState);
    }

    private boolean isMovieFavoured() {
        Cursor cursor = getContentResolver().query(CONTENT_URI, null, "movie_id=?", new
                String[]{String.valueOf(movie.getId())}, null);
        return cursor.getCount() > 0;
    }

    private void setUpTrailers(String trailersJson) {
        if (!trailersJson.isEmpty()) {
            List<Trailer> trailers = null;
            try {
                trailers = JsonParser.getMovieTrailersFromJson(trailersJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (trailers != null && !trailers.isEmpty()) {
                MovieTrailersAdapter adapter = new MovieTrailersAdapter(this, trailers);
                trailersList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                trailersList.setAdapter(adapter);

                trailersLabel.setVisibility(View.VISIBLE);
                trailersList.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setUpReviews(String reviewsJson) {
        if (!reviewsJson.isEmpty()) {
            List<Review> reviews = null;
            try {
                reviews = JsonParser.getMovieReviewsFromJson(reviewsJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (reviews != null && !reviews.isEmpty()) {
                MovieReviewsAdapter adapter = new MovieReviewsAdapter(reviews);
                reviewsList.setLayoutManager(new LinearLayoutManager(this));
                reviewsList.setAdapter(adapter);

                reviewsLabel.setVisibility(View.VISIBLE);
                reviewsList.setVisibility(View.VISIBLE);

            }
        }
    }
}
