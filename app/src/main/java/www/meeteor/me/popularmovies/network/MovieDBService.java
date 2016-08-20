package www.meeteor.me.popularmovies.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Meet on 19-06-2016.
 */
public interface MovieDBService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            // .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("{sort_order}")
    Call<ResponseBody> getMovies(@Path("sort_order") String sort_order, @Query("api_key") String api_key, @Query("page") int page);

    @GET("{id}/videos")
    Call<ResponseBody> getMovieVideos(@Path("id") String movie_id, @Query("api_key") String api_key);

    @GET("{id}/reviews")
    Call<ResponseBody> getMovieReviews(@Path("id") String movie_id, @Query("api_key") String api_key);
}
