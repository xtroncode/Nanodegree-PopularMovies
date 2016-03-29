package www.meeteor.me.popularmovies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by meet on 28/3/16.
 *
 */
public class PosterRVAdapter extends RecyclerView.Adapter<PosterRVAdapter.ViewHolder>{


    private List<Movie> mMovieList;
    public PosterRVAdapter(List<Movie> mMovieList)  {
       this.mMovieList = mMovieList;

    }





    @Override
    public PosterRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View posterView = inflater.inflate(R.layout.poster, parent, false);

        // Return a new holder instance
        return new ViewHolder(posterView);
    }

    @Override
    public void onBindViewHolder(PosterRVAdapter.ViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        String imgStr = "https://image.tmdb.org/t/p/w185" + movie.posterUrl;
        //Log.d("ImgStr",imgStr);
        Picasso.with(holder.posterImage.getContext()).load(imgStr).placeholder(R.mipmap.ic_launcher).into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView posterImage;
        public ViewHolder(View itemView) {
            super(itemView);

            posterImage = (ImageView) itemView.findViewById(R.id.poster_view);
        }
    }

}
