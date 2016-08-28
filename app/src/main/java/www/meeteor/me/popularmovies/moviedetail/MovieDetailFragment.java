package www.meeteor.me.popularmovies.moviedetail;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by meet on 8/4/16.
 */
public class MovieDetailFragment extends Fragment {
    public static final String TAG = "detail_fragment";
    private static final String ARG_PARAM = "Movie";
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @Bind(R.id.movie_title) TextView movieTitle;
    @Bind(R.id.movie_overview) TextView movieOverview;
    @Bind(R.id.movie_rating) RatingBar movieRating;
    @Bind(R.id.movie_release_date) TextView movieReleaseDate;
    @Bind(R.id.backdrop_poster) ImageView movieBackdropPoster;
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
    private Movie movie;

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM,movie);
        movieDetailFragment.setArguments(args);
        return movieDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            movie = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.movie_detail_fragment,container,false);

        ButterKnife.bind(this,view);

        if(movie != null) {
            mVideoList = new ArrayList<Video>();
            mReviewList = new ArrayList<Review>();
            videosRVAdapter = new VideosRVAdapter(mVideoList, getContext());
            reviewsRVAdapter = new ReviewsRVAdapter(mReviewList, getContext());
            mVideosRV.setAdapter(videosRVAdapter);
            mReviewsRV.setAdapter(reviewsRVAdapter);
            mVideosRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mReviewsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            setUpDetailUI();
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
            getActivity().setTitle(movie.getTitle());
            view.setVisibility(View.VISIBLE);
        }
        else{
            view.setVisibility(View.INVISIBLE);
        }
        return view;
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

        getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);

    }

    private void deleteMovieFromFavorite(Movie movie) {
        getActivity().getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(movie.getId()), "movie_id=" + Integer.toString(movie.getId()), null);
    }
    private void setUpDetailUI() {
        String imageUrlString = Constants.IMAGE_BASE_URL+Constants.BACKDROP_IMAGE_SIZE+Constants.BACKSLASH + movie.getBackdropPath();

        mCollapsingToolbar.setTitle(movie.getTitle());
        Picasso.with(getContext()).load(imageUrlString).placeholder(R.mipmap.ic_launcher).into(movieBackdropPoster);
        movieTitle.setText(movie.getOriginalTitle());
        movieOverview.setText(movie.getOverview());
        float voteAverage = (float) movie.getVoteAverage()/2;
        movieRating.setRating(voteAverage);
        String releaseDate = "Release date: "+ movie.getReleaseDate();
        movieReleaseDate.setText(releaseDate);
        showMovieVideos(movie.getId());
        showMovieReviews(movie.getId());
        movie.isFavorite = getActivity().getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(movie.getId()), null, null, null, null).getCount() != 0;
        if (movie.isFavorite) {
            mFab.setImageResource(R.drawable.ic_favorite_red_a700_24dp);
        } else {
            mFab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private void showMovieVideos(int movieId) {
        if (Util.isNetworkConnected(getContext())) {
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
        if (Util.isNetworkConnected(getContext())) {
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


}
