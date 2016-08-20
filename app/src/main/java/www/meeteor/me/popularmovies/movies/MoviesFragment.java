package www.meeteor.me.popularmovies.movies;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import www.meeteor.me.popularmovies.moviedetail.MovieDetailActivity;
import www.meeteor.me.popularmovies.moviedetail.MovieDetailFragment;
import www.meeteor.me.popularmovies.network.MovieDBService;
import www.meeteor.me.popularmovies.util.Constants;
import www.meeteor.me.popularmovies.util.EndlessRecyclerViewScrollListener;
import www.meeteor.me.popularmovies.util.Util;

/**
 * Created by meet on 1/4/16.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.movies_rv)
    RecyclerView mMoviesRV;
    @Bind(R.id.loading)
    ProgressBar mProgressBar;
    MovieItemListener movieItemListener = new MovieItemListener() {
        @Override
        public void onMovieClick(Movie movie) {
            showMovieDetailUi(movie);
        }
    };
    private ArrayList<Movie> mMovieList;
    private MoviesRVAdapter moviesRVAdapter;
    private View view;
    private String SORT_PARAMETER = "popular";

    public static MoviesFragment newInstance(){
        return new MoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieList = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState != null) {
            ArrayList<Movie> arrayList = savedInstanceState.getParcelableArrayList("StateMovies");
            mMovieList.addAll(arrayList);
            populateRecyclerView();
            Log.d("ONE", "Exec");
        } else {
            try {
                getMovies(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.poster_fragment, container, true);

        ButterKnife.bind(this, view);

        int numOfColumns = getResources().getInteger(R.integer.num_of_columns);

        moviesRVAdapter = new MoviesRVAdapter(mMovieList, movieItemListener);
        if (mMoviesRV != null) {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numOfColumns, GridLayoutManager.VERTICAL, false);
            mMoviesRV.setLayoutManager(gridLayoutManager);
            mMoviesRV.setAdapter(moviesRVAdapter);
            mMoviesRV.setHasFixedSize(true);
            mMoviesRV.addOnScrollListener(getScrollListener(gridLayoutManager));

        }


        return view;
    }

    private EndlessRecyclerViewScrollListener getScrollListener(GridLayoutManager gridLayoutManager) {
        return new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                try {
                    Log.d("PAGE", Integer.toString(page));
                    getMovies(page);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean hasNetworkConnectivity() {
                boolean networkStatus = Util.isNetworkConnected(getContext());
                if (!networkStatus) {
                    Snackbar.make(view, "No Network Connection", Snackbar.LENGTH_SHORT).show();
                }
                return networkStatus;
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_movies, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_by_popularity) {
            SORT_PARAMETER = "popular";
            resetMovieList();
            return true;
        }

        if (id == R.id.action_sort_by_rating) {
            SORT_PARAMETER = "top_rated";
            resetMovieList();
            return true;
        }

        if (id == R.id.action_get_favorites) {
            SORT_PARAMETER = "favorite";
            resetMovieList();
            return true;
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("StateMovies", mMovieList);
    }

    public void handleAsyncResponse(JSONArray jsonArray) {
        ArrayList<Movie> responseMovieList = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    responseMovieList.add(new Movie(jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        mMovieList.addAll(responseMovieList);
        populateRecyclerView();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.MOVIE_PROJECTIONS, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Movie> favoriteMovieList = new ArrayList<>();
        if (data != null && data.moveToFirst()) {
            do {
                int id = data.getInt(MovieContract.MovieEntry.MOVIE_ID_COl_INDEX);
                String title = data.getString(MovieContract.MovieEntry.TITLE_COl_INDEX);
                String posterPath = data.getString(MovieContract.MovieEntry.POSTER_PATH_COl_INDEX);
                String overview = data.getString(MovieContract.MovieEntry.OVERVIEW_COl_INDEX);
                double voteAverage = Double.parseDouble(data.getString(MovieContract.MovieEntry.VOTE_AVERAGE_COl_INDEX));
                String releaseDate = data.getString(MovieContract.MovieEntry.RELEASE_DATE_COl_INDEX);
                String backdropPath = data.getString(MovieContract.MovieEntry.BACKDROP_PATH_COl_INDEX);
                String originalTitle = data.getString(MovieContract.MovieEntry.ORIGINAL_TITLE_COl_INDEX);
                Movie movie = new Movie(id, posterPath, title, backdropPath, overview, originalTitle, voteAverage, releaseDate);
                favoriteMovieList.add(movie);
            } while (data.moveToNext());
        }
        if (SORT_PARAMETER.equals("favorite")) {
            mMovieList.clear();
            mMovieList.addAll(favoriteMovieList);
            populateRecyclerView();
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void populateRecyclerView() {
        moviesRVAdapter.notifyDataSetChanged();
    }

    private void getMovies(final int page) throws IOException {

        mProgressBar.setVisibility(View.VISIBLE);
        if (Util.isNetworkConnected(getContext())) {
            // GetMoviesTask getMoviesTask = new GetMoviesTask(this,SORT_PARAMETER);
            //getMoviesTask.execute(page);
            MovieDBService service = MovieDBService.retrofit.create(MovieDBService.class);
            Call<ResponseBody> call = service.getMovies(SORT_PARAMETER, Constants.MOVIEDB_API_KEY, page);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        JSONObject rObj = new JSONObject(response.body().string());
                        handleAsyncResponse(rObj.getJSONArray("results"));
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

            Snackbar.make(view, "No Network Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                getMovies(page);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).show();
        }

    }

    private void resetMovieList() {
        mMovieList.clear();
        if (SORT_PARAMETER != "favorite") {
            try {
                mMoviesRV.clearOnScrollListeners();
                mMoviesRV.addOnScrollListener(getScrollListener((GridLayoutManager) mMoviesRV.getLayoutManager()));
                getMovies(1);
            } catch (IOException e) {
                e.printStackTrace();
        }
        }
        else{
            mMoviesRV.clearOnScrollListeners();
            getLoaderManager().initLoader(0, null, this);
        }
        mMoviesRV.scrollToPosition(0);
    }

    public void showMovieDetailUi(Movie movie) {
        if((((MoviesActivity)getActivity()).mIsTwoPane)){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movie_detail_fragment_container,MovieDetailFragment.newInstance(movie),MovieDetailFragment.TAG)
                    .commit();
        }
        else{
            Intent intent = new Intent(getContext(),MovieDetailActivity.class);
            intent.putExtra("Movie",movie);
            startActivity(intent);
        }
    }


    interface MovieItemListener {
        void onMovieClick(Movie movie);
    }


}
