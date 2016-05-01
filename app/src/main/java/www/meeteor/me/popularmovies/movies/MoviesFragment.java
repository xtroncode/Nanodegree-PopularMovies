package www.meeteor.me.popularmovies.movies;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import www.meeteor.me.popularmovies.R;
import www.meeteor.me.popularmovies.data.GetMoviesTask;
import www.meeteor.me.popularmovies.data.Movie;
import www.meeteor.me.popularmovies.moviedetail.MovieDetailActivity;
import www.meeteor.me.popularmovies.moviedetail.MovieDetailFragment;
import www.meeteor.me.popularmovies.util.EndlessRecyclerViewScrollListener;
import www.meeteor.me.popularmovies.util.Util;

/**
 * Created by meet on 1/4/16.
 */
public class MoviesFragment extends Fragment implements GetMoviesTask.OnExecutionComplete {

    private ArrayList<Movie> mMovieList;
    private MoviesRVAdapter moviesRVAdapter;
    private View view;
    private String SORT_PARAMETER = "popular";

    @Bind(R.id.movies_rv) RecyclerView  mMoviesRV;

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

        if(savedInstanceState != null){
            ArrayList<Movie> arrayList = savedInstanceState.getParcelableArrayList("StateMovies");
            mMovieList.addAll(arrayList);
            populateRecyclerView();
            Log.d("ONE","Exec");
        }
        else
        {
            Log.d("ONE","Exec");
            getMovies(1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        view =  inflater.inflate(R.layout.poster_fragment,container,true);

        ButterKnife.bind(this,view);
        Log.d("ONE","Exec");
        int numOfColumns = getResources().getInteger(R.integer.num_of_columns);

        moviesRVAdapter = new MoviesRVAdapter(mMovieList,movieItemListener);
        if (mMoviesRV != null) {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),numOfColumns,GridLayoutManager.VERTICAL,false);
            mMoviesRV.setLayoutManager(gridLayoutManager);
            mMoviesRV.setAdapter(moviesRVAdapter);
            mMoviesRV.setHasFixedSize(true);
            mMoviesRV.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager){
                @Override
                public void onLoadMore(int page, int totalItemsCount) {

                    getMovies(page);
                }

                @Override
                public boolean hasNetworkConnectivity() {
                    boolean networkStatus =  Util.isNetworkConnected(getContext());
                    if(!networkStatus) {
                        Snackbar.make(view,"No Network Connetion",Snackbar.LENGTH_SHORT).show();
                    }
                    return  networkStatus;
                }
            });

        }


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_movies, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_sort_by_popularity){
            SORT_PARAMETER = "popular";
            resetMovieList();
            return true;
        }

        if(id== R.id.action_sort_by_rating){
            SORT_PARAMETER = "top_rated";
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

    @Override
    public void handleAsyncResponse(ArrayList<Movie> responseMovieList) {
        mMovieList.addAll(responseMovieList);
        populateRecyclerView();
    }

    interface MovieItemListener {
        void onMovieClick(Movie movie);
    }


    MovieItemListener movieItemListener = new MovieItemListener() {
        @Override
        public void onMovieClick(Movie movie) {
            showMovieDetailUi(movie);
        }
    };


    private void populateRecyclerView() {
        moviesRVAdapter.notifyDataSetChanged();
    }

    private void getMovies(final int page) {
        if(Util.isNetworkConnected(getContext())) {
            GetMoviesTask getMoviesTask = new GetMoviesTask(this,SORT_PARAMETER);
            getMoviesTask.execute(page);
        }
        else{

            Snackbar.make(view,"No Network Connection",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMovies(page);
                        }
                    }).show();
        }

    }

    private void resetMovieList() {
        mMovieList.clear();
        getMovies(1);
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


}
