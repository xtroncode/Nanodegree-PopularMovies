package www.meeteor.me.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by meet on 28/3/16.
 *
 */
public final class Movie implements Parcelable {


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
    public boolean isFavorite = false;
    public int _id = 0;
    private String posterPath;
    private String title;
    private String overview;
    private String backdropPath;
    private boolean adult;
    private int[] genreIds;
    private int id;
    private String originalTitle;
    private String originalLanguage;
    private double popularity;
    private int voteCount;
    private boolean video;
    private double voteAverage;
    private String releaseDate;

    public Movie(JSONObject movie) throws JSONException {
        posterPath = movie.getString("poster_path");
        title = movie.getString("title");
        overview  = movie.getString("overview");
        backdropPath = movie.getString("backdrop_path");
        id = movie.getInt("id");
        originalTitle = movie.getString("original_title");
        voteAverage =  movie.getDouble("vote_average");
        releaseDate = movie.getString("release_date");
    }


    public Movie(int id, String posterPath, String title, String backdropPath, String overview, String originalTitle, double voteAverage, String releaseDate) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.originalTitle = originalTitle;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        title = in.readString();
        overview = in.readString();
        backdropPath = in.readString();
        id = in.readInt();
        originalTitle = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(backdropPath);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }


    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
