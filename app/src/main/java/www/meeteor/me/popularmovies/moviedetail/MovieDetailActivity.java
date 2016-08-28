package www.meeteor.me.popularmovies.moviedetail;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.meeteor.me.popularmovies.R;
import www.meeteor.me.popularmovies.data.Movie;
import www.meeteor.me.popularmovies.data.MovieContract;
import www.meeteor.me.popularmovies.data.Review;
import www.meeteor.me.popularmovies.data.Video;
import www.meeteor.me.popularmovies.network.MovieDBService;
import www.meeteor.me.popularmovies.util.Constants;
import www.meeteor.me.popularmovies.util.Util;

/**
 * Created by meet on 30/3/16.
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Bind(R.id.movie_title) TextView movieTitle;
    @Bind(R.id.movie_overview) TextView movieOverview;
    @Bind(R.id.movie_rating) RatingBar movieRating;
    @Bind(R.id.backdrop_poster) ImageView movieBackdropPoster;
    @Bind(R.id.movie_release_date) TextView movieReleaseDate;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.videos_rv)
    RecyclerView mVideosRV;
    @Bind(R.id.reviews_rv)
    RecyclerView mReviewsRV;

    private ArrayList<Video> mVideoList;
    private ArrayList<Review> mReviewList;
    private VideosRVAdapter videosRVAdapter;
    private ReviewsRVAdapter reviewsRVAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        final Movie movie = getIntent().getParcelableExtra("Movie");
        mVideoList = new ArrayList<Video>();
        mReviewList = new ArrayList<Review>();
        videosRVAdapter = new VideosRVAdapter(mVideoList, this);
        reviewsRVAdapter = new ReviewsRVAdapter(mReviewList, this);
        mVideosRV.setAdapter(videosRVAdapter);
        mReviewsRV.setAdapter(reviewsRVAdapter);
        mVideosRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mReviewsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setUpDetailUI(movie);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie.isFavorite) {
                    movie.isFavorite = false;
                    deleteMovieFromFavorite(movie);
                    mFab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                } else {
                    addMovieToFavorite(movie);
                    mFab.setImageResource(R.drawable.ic_favorite_red_a700_24dp);
                    movie.isFavorite = true;
                }
            }
        });



    }

    private void addMovieToFavorite(Movie movie) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        movieValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());

        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);

    }

    private void deleteMovieFromFavorite(Movie movie) {
        getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(movie.getId()), "movie_id=" + Integer.toString(movie.getId()), null);
        Log.d("M", "deleted from fav");
    }

    private void setUpDetailUI(Movie movie) {
        String imageUrlString = Constants.IMAGE_BASE_URL+Constants.BACKDROP_IMAGE_SIZE+Constants.BACKSLASH + movie.getBackdropPath();
        mCollapsingToolbar.setTitle(movie.getTitle());
        Picasso.with(this).load(imageUrlString).placeholder(R.mipmap.ic_launcher).into(movieBackdropPoster);
        movieTitle.setText(movie.getOriginalTitle());
        movieOverview.setText(movie.getOverview());
        float voteAverage = (float) movie.getVoteAverage()/2;
        movieRating.setRating(voteAverage);
        String releaseDate = "Release date: "+ movie.getReleaseDate();
        movieReleaseDate.setText(releaseDate);
        showMovieVideos(movie.getId());
        showMovieReviews(movie.getId());
        movie.isFavorite = getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(movie.getId()), null, null, null, null).getCount() != 0;
        if (movie.isFavorite) {
            mFab.setImageResource(R.drawable.ic_favorite_red_a700_24dp);
        } else {
            mFab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private void showMovieVideos(int movieId) {
        if (Util.isNetworkConnected(getApplicationContext())) {
            MovieDBService service = MovieDBService.retrofit.create(MovieDBService.class);
            Call<ResponseBody> call = service.getMovieVideos(movieId, Constants.MOVIEDB_API_KEY);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        JSONObject rObj = new JSONObject(response.body().string());
                        populateVideos(rObj.getJSONArray("results"));
                    } catch (IOException | JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("response", t.toString());
                }
            });


        } else {
        }
    }


    private void showMovieReviews(int movieId) {
        if (Util.isNetworkConnected(getApplicationContext())) {
            MovieDBService service = MovieDBService.retrofit.create(MovieDBService.class);
            Call<ResponseBody> call = service.getMovieReviews(movieId, Constants.MOVIEDB_API_KEY);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        JSONObject rObj = new JSONObject(response.body().string());
                        populateReviews(rObj.getJSONArray("results"));
                    } catch (IOException | JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("response", t.toString());
                }
            });


        } else {
        }
    }

    private void populateVideos(JSONArray results) {

        if (results != null) {
            for (int i = 0; i < results.length(); i++) {
                try {
                    mVideoList.add(new Video(results.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            videosRVAdapter.notifyDataSetChanged();
        }
    }

    private void populateReviews(JSONArray results) {


        if (results != null) {
            for (int i = 0; i < results.length(); i++) {
                try {
                    mReviewList.add(new Review(results.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            reviewsRVAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
