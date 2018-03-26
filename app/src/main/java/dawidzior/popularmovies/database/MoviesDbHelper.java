package dawidzior.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_MOVIE_ID;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_ORIGINAL_TITLE;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_OVERVIEW;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_POSTER_PATH;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_RELEASE_DATE;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_REVIEWS_JSON;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_TITLE;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_TRAILERS_JSON;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.COLUMN_USER_RATING;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.TABLE_MOVIES;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry._ID;

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviesDatabase";
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIES + "(" +
                _ID + " INTEGER PRIMARY KEY," + // Define a primary key
                COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                COLUMN_USER_RATING + " REAL NOT NULL, " +
                COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                COLUMN_TRAILERS_JSON + " TEXT NOT NULL DEFAULT '',  " +
                COLUMN_REVIEWS_JSON + " TEXT NOT NULL DEFAULT ''" +
                ")";

        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
            onCreate(db);
        }
    }
}