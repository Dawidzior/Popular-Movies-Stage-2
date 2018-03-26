package dawidzior.popularmovies.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dawidzior.popularmovies.model.Movie;
import dawidzior.popularmovies.model.Review;
import dawidzior.popularmovies.model.Trailer;

public class JsonParser {
    private static final String ID = "id";
    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String POSTER = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";

    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";

    private static final String TRAILER_NAME = "name";
    private static final String TRAILER_KEY = "key";
    private static final String TRAILER_SITE = "site";

    public static List<Movie> getMoviesFromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);

        ArrayList<Movie> popularMovieList = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonMovieObject = jsonArray.getJSONObject(i);
            Movie popularMovie = new Movie();
            popularMovie.setId(jsonMovieObject.optInt(ID));
            popularMovie.setTitle(jsonMovieObject.optString(TITLE));
            popularMovie.setOriginalTitle(jsonMovieObject.optString(ORIGINAL_TITLE));
            popularMovie.setOverview(jsonMovieObject.optString(OVERVIEW));
            popularMovie.setPoster(TextUtils
                    .substring(jsonMovieObject.optString(POSTER), 1, jsonMovieObject.optString(POSTER).length()));
            popularMovie.setUserRating(jsonMovieObject.getDouble(VOTE_AVERAGE));
            popularMovie.setReleaseDate(jsonMovieObject.optString(RELEASE_DATE));

            URL reviewsUrl =
                    NetworkUtils.buildMovieUrl(String.valueOf(jsonMovieObject.optInt(ID)), NetworkUtils.REVIEWS);
            try {
                String requestReviewsJson = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
                popularMovie.setReviewsJson(requestReviewsJson);
            } catch (Exception e) {
                e.printStackTrace();
            }

            URL trailersUrl = NetworkUtils.buildMovieUrl(String.valueOf(jsonMovieObject.optInt(ID)), NetworkUtils
                    .VIDEOS);
            try {
                String requestTrailersJson = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
                popularMovie.setTrailersJson(requestTrailersJson);
            } catch (Exception e) {
                e.printStackTrace();
            }

            popularMovieList.add(popularMovie);
        }
        return popularMovieList;
    }

    public static List<Trailer> getMovieTrailersFromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);

        ArrayList<Trailer> trailersList = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonTrailerObject = jsonArray.getJSONObject(i);
            Trailer trailer = new Trailer();
            trailer.setName(jsonTrailerObject.optString(TRAILER_NAME));
            trailer.setKey(jsonTrailerObject.optString(TRAILER_KEY));
            trailer.setSite(jsonTrailerObject.optString(TRAILER_SITE));
            trailersList.add(trailer);
        }
        return trailersList;
    }

    public static List<Review> getMovieReviewsFromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);

        ArrayList<Review> reviewsList = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonReviewObject = jsonArray.getJSONObject(i);
            Review review = new Review();
            review.setId(jsonReviewObject.optInt(ID));
            review.setAuthor(jsonReviewObject.optString(AUTHOR));
            review.setContent(jsonReviewObject.optString(CONTENT));
            reviewsList.add(review);
        }
        return reviewsList;
    }
}
