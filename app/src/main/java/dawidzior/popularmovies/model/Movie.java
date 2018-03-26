package dawidzior.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private int id;
    private String title;
    private String originalTitle;
    private String poster;
    private String overview;
    private Double userRating;
    private String releaseDate;
    private String trailersJson;
    private String reviewsJson;

    public Movie() {
    }

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        originalTitle = in.readString();
        poster = in.readString();
        overview = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
        trailersJson = in.readString();
        reviewsJson = in.readString();
    }

    public Movie(int id, String title, String originalTitle, String poster, String overview, double userRating,
                 String releaseDate, String trailersJson, String reviewsJson) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.poster = poster;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.trailersJson = trailersJson;
        this.reviewsJson = reviewsJson;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(originalTitle);
        parcel.writeString(poster);
        parcel.writeString(overview);
        parcel.writeDouble(userRating);
        parcel.writeString(releaseDate);
        parcel.writeString(trailersJson);
        parcel.writeString(reviewsJson);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String plotSynopsis) {
        this.overview = plotSynopsis;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTrailersJson() {
        return trailersJson;
    }

    public void setTrailersJson(String trailersJson) {
        this.trailersJson = trailersJson;
    }

    public String getReviewsJson() {
        return reviewsJson;
    }

    public void setReviewsJson(String reviewsJson) {
        this.reviewsJson = reviewsJson;
    }
}

