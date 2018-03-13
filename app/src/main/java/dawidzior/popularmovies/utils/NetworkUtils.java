package dawidzior.popularmovies.utils;

import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import dawidzior.popularmovies.BuildConfig;

public class NetworkUtils {
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String API_KEY_PARAM = "api_key";

    private static final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    @Nullable
    public static URL buildTheMovieDbUrl(String queryType) {
        Uri moviesQueryUri =
                Uri.parse(MOVIE_BASE_URL).buildUpon().appendPath(queryType).appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

        try {
            return new URL(moviesQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                String response = scanner.next();
                scanner.close();
                return response;
            }
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
