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
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import www.meeteor.me.popularmovies.PosterTask;
import www.meeteor.me.popularmovies.R;
import www.meeteor.me.popularmovies.data.Movie;
import www.meeteor.me.popularmovies.moviedetail.MovieDetailActivity;
import www.meeteor.me.popularmovies.moviedetail.MovieDetailFragment;
import www.meeteor.me.popularmovies.util.EndlessRecyclerViewScrollListener;
import www.meeteor.me.popularmovies.util.Util;

/**
 * Created by meet on 1/4/16.
 */
public class MoviesFragment extends Fragment implements PosterTask.OnExecutionComplete {

    private ArrayList<Movie> mMovieList;
    private MoviesRVAdapter moviesRVAdapter;
    private View view;
    public static MoviesFragment newInstance(){
        return new MoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        if(savedInstanceState != null){

            Log.d("Restore","From saved state");
            ArrayList<Movie> arrayList = savedInstanceState.getParcelableArrayList("StatePosters");
            mMovieList.addAll(arrayList);
            populateRecyclerView();
        }
        else
        {
            Log.d("ONE","Exec");
            getPosters(1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.poster_fragment,container,true);
        int numOfColumns = getResources().getInteger(R.integer.num_of_columns);
        RecyclerView posterRv = (RecyclerView) view.findViewById(R.id.poster_rv);
        moviesRVAdapter = new MoviesRVAdapter(mMovieList,movieItemListener);
        if (posterRv != null) {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),numOfColumns,GridLayoutManager.VERTICAL,false);
            posterRv.setLayoutManager(gridLayoutManager);
            posterRv.setAdapter(moviesRVAdapter);
            posterRv.setHasFixedSize(true);
            posterRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager){
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    Log.d("Scroll-load",Integer.toString(page));
                    getPosters(page);
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



    private void getPosters(final int page) {
        if(Util.isNetworkConnected(getContext())) {
            PosterTask posterTask = new PosterTask(this);
            posterTask.execute(page);
        }
        else{

            Snackbar.make(view,"No Network Connection",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getPosters(page);
                        }
                    }).show();
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("StatePosters", mMovieList);
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



    @Override
    public void handleAsyncResponse(ArrayList<Movie> responseMovieList) {
        mMovieList.addAll(responseMovieList);
        populateRecyclerView();
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
