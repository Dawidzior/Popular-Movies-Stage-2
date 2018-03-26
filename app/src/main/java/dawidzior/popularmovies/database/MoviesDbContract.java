package dawidzior.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesDbContract {

    static final String AUTHORITY = "dawidzior.popularmovies";
    static final String URI = "content://" + AUTHORITY;
    static final String PATH = "movies";

    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse(URI).buildUpon().appendPath(PATH).build();

        public static final String TABLE_MOVIES = "movies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_USER_RATING = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TRAILERS_JSON = "trailers_json";
        public static final String COLUMN_REVIEWS_JSON = "reviews_json";
    }
}
