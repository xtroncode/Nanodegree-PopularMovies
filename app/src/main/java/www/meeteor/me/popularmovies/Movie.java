package www.meeteor.me.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by meet on 28/3/16.
 *
 */
public class Movie {
    public String posterUrl;

    public Movie(JSONObject movie) throws JSONException {
        posterUrl =movie.getString("poster_path");
    }
}
