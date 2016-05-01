package www.meeteor.me.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by meet on 28/3/16.
 *
 */
public final class Movie implements Parcelable {


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
        adult = movie.getBoolean("adult");
        id = movie.getInt("id");
        originalTitle = movie.getString("original_title");
        originalLanguage = movie.getString("original_language");
        popularity = movie.getDouble("popularity");
        voteCount = movie.getInt("vote_count");
        voteAverage =  movie.getDouble("vote_average");
        releaseDate = movie.getString("release_date");
        video = movie.getBoolean("video");
        JSONArray jsonArray = movie.getJSONArray("genre_ids");
        genreIds = new int[jsonArray.length()];
        for(int i = 0 ; i < jsonArray.length();i++){
            genreIds[i] = (int) jsonArray.get(i);
        }
    }


    protected Movie(Parcel in) {
        posterPath = in.readString();
        title = in.readString();
        overview = in.readString();
        backdropPath = in.readString();
        adult = in.readByte() != 0;
        genreIds = in.createIntArray();
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        video = in.readByte() != 0;
        voteAverage = in.readDouble();
        releaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(backdropPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeIntArray(genreIds);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public boolean isAdult() {
        return adult;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
