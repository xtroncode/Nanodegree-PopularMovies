package www.meeteor.me.popularmovies.moviedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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

/**
 * Created by meet on 8/4/16.
 */
public class MovieDetailFragment extends Fragment {
    public static final String TAG = "detail_fragment";
    private static final String ARG_PARAM = "Movie";
    private Movie movie;

    @Bind(R.id.movie_title) TextView movieTitle;
    @Bind(R.id.movie_overview) TextView movieOverview;
    @Bind(R.id.movie_rating) RatingBar movieRating;
    @Bind(R.id.backdrop_poster) ImageView movieBackdropPoster;

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

            String imgStr = "https://image.tmdb.org/t/p/w342" + movie.getBackdropPath();
            CollapsingToolbarLayout collapsingToolbar =
                    (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
            if (collapsingToolbar != null) {
                collapsingToolbar.setTitle(movie.getTitle());

            }
            Picasso.with(getContext()).load(imgStr).placeholder(R.mipmap.ic_launcher).into(movieBackdropPoster);
            movieTitle.setText(movie.getOriginalTitle());
            movieOverview.setText(movie.getOverview());
            movieRating.setRating((float) movie.getVoteAverage()/2);

            getActivity().setTitle(movie.getTitle());
            view.setVisibility(View.VISIBLE);
        }
        else{
            view.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
