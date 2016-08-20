package www.meeteor.me.popularmovies.moviedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import www.meeteor.me.popularmovies.R;
import www.meeteor.me.popularmovies.data.Movie;
import www.meeteor.me.popularmovies.util.Constants;

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

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
            setUpDetailUI();
            getActivity().setTitle(movie.getTitle());
            view.setVisibility(View.VISIBLE);
        }
        else{
            view.setVisibility(View.INVISIBLE);
        }
        return view;
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
    }

}
