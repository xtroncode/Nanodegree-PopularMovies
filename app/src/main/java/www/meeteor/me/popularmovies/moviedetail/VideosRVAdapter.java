package www.meeteor.me.popularmovies.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import www.meeteor.me.popularmovies.R;
import www.meeteor.me.popularmovies.data.Video;

/**
 * Created by Meet on 21-08-2016.
 */
public class VideosRVAdapter extends RecyclerView.Adapter<VideosRVAdapter.ViewHolder> {

    private ArrayList<Video> mVideos;
    private Context mContext;
    public VideosRVAdapter(ArrayList<Video> videos, Context context) {
        mVideos = videos;
        mContext = context;
    }

    @Override
    public VideosRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View videoView = inflater.inflate(R.layout.video, parent, false);
        return new ViewHolder(videoView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = mVideos.get(position);
        String imgUrl = "http://img.youtube.com/vi/" + video.getKey() + "/default.jpg";
        Picasso.with(holder.videoImage.getContext()).load(imgUrl).placeholder(R.mipmap.ic_launcher).into(holder.videoImage);
        holder.videoTitle.setText(video.getName());
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView videoTitle;
        public ImageView videoImage;

        public ViewHolder(View itemView) {
            super(itemView);

            videoImage = (ImageView) itemView.findViewById(R.id.video_image);
            videoTitle = (TextView) itemView.findViewById(R.id.video_title);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Video video = mVideos.get(position);
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
        }
    }
}
