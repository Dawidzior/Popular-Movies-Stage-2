package dawidzior.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static dawidzior.popularmovies.database.MoviesDbContract.AUTHORITY;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.CONTENT_URI;
import static dawidzior.popularmovies.database.MoviesDbContract.MoviesEntry.TABLE_MOVIES;
import static dawidzior.popularmovies.database.MoviesDbContract.PATH;

public class MoviesDbProvider extends ContentProvider {

    public static final int ALL_MOVIES = 100;
    public static final int MOVIE_ID = 101;

    private SQLiteDatabase db;
    private UriMatcher uriMatcher = buildMatcher();

    public static UriMatcher buildMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, ALL_MOVIES);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", MOVIE_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        MoviesDbHelper moviesDbHelper = new MoviesDbHelper(context);
        db = moviesDbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        switch (uriMatcher.match(uri)) {
            case ALL_MOVIES:
                long rowID = db.insert(TABLE_MOVIES, null, contentValues);
                if (rowID > 0) {
                    Uri contentUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(contentUri, null);
                    return uri;
                } else {
                    throw new SQLException("Failed to add a record into " + uri);
                }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_MOVIES);

        switch (uriMatcher.match(uri)) {
            case ALL_MOVIES:
                Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented in Popular Movies.");
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case MOVIE_ID:
                count = db.delete(TABLE_MOVIES, "movie_id=?", new String[]{uri.getPathSegments().get(1)});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        throw new RuntimeException("Not implemented in Popular Movies.");
    }
}
