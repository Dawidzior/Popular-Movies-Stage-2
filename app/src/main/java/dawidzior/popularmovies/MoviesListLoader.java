package dawidzior.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import dawidzior.popularmovies.model.Movie;
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
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry._ID;

public class MoviesListLoader extends AsyncTaskLoader<List<Movie>> {

    private String internetRequestSortByKey;
    private List<Movie> moviesList = null;

    MoviesListLoader(Context context, Bundle bundle) {
        super(context);
        if (bundle != null) internetRequestSortByKey = bundle.getString(MainActivity.SORT_BY_KEY);
    }

    @Override
    public List<Movie> loadInBackground() {

        if (internetRequestSortByKey != null) {
            // Internet request.
            try {
                String requestJson =
                        NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildTheMovieDbUrl(internetRequestSortByKey));
                moviesList = JsonParser.getMoviesFromJson(requestJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Database request.
            Cursor cursor = getContext().getContentResolver().query(CONTENT_URI, null, null, null, _ID);
            moviesList = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    moviesList.add(new Movie(cursor.getInt(cursor.getColumnIndex(COLUMN_MOVIE_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_ORIGINAL_TITLE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)),
                            cursor.getDouble(cursor.getColumnIndex(COLUMN_USER_RATING)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TRAILERS_JSON)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_REVIEWS_JSON))
                    ));
                } while (cursor.moveToNext());
                cursor.close();
            }
        }

        return moviesList;
    }
}
