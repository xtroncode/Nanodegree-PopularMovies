package www.meeteor.me.popularmovies.moviedetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import www.meeteor.me.popularmovies.R;
import www.meeteor.me.popularmovies.data.Review;

/**
 * Created by Meet on 21-08-2016.
 */
public class ReviewsRVAdapter extends RecyclerView.Adapter<ReviewsRVAdapter.ViewHolder> {

    private ArrayList<Review> mReviews;
    private Context mContext;

    public ReviewsRVAdapter(ArrayList<Review> reviews, Context context) {
        mReviews = reviews;
        mContext = context;
    }

    @Override
    public ReviewsRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View reviewView = inflater.inflate(R.layout.review, parent, false);
        return new ViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
