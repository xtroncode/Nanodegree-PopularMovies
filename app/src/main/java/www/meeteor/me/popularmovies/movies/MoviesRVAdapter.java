package www.meeteor.me.popularmovies.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import www.meeteor.me.popularmovies.R;
import www.meeteor.me.popularmovies.data.Movie;

/**
 * Created by meet on 9/4/16.
 */
class MoviesRVAdapter extends RecyclerView.Adapter<MoviesRVAdapter.ViewHolder> {


    private List<Movie> mMovieList;
    private MoviesFragment.MovieItemListener movieItemListener;

    public MoviesRVAdapter(List<Movie> mMovieList, MoviesFragment.MovieItemListener movieItemListener) {
        this.mMovieList = mMovieList;
        this.movieItemListener = movieItemListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View posterView = inflater.inflate(R.layout.poster, parent, false);

        // Return a new holder instance
        return new ViewHolder(posterView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        String imgStr = "https://image.tmdb.org/t/p/w185" + movie.getPosterPath();
        //Log.d("ImgStr",imgStr);
        Picasso.with(holder.posterImage.getContext()).load(imgStr).placeholder(R.mipmap.ic_launcher).into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView posterImage;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImage = (ImageView) itemView.findViewById(R.id.poster_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Movie movie = mMovieList.get(position);
            movieItemListener.onMovieClick(movie);

        }
    }

}
