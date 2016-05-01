package www.meeteor.me.popularmovies.moviedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
 * Created by meet on 30/3/16.
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Bind(R.id.movie_title) TextView movieTitle;
    @Bind(R.id.movie_overview) TextView movieOverview;
    @Bind(R.id.movie_rating) RatingBar movieRating;
    @Bind(R.id.backdrop_poster) ImageView movieBackdropPoster;
    @Bind(R.id.movie_release_date) TextView movieReleaseDate;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Movie movie = getIntent().getParcelableExtra("Movie");

        setUpDetailUI(movie);



    }

    private void setUpDetailUI(Movie movie) {
        String imageUrlString = Constants.IMAGE_BASE_URL+Constants.BACKDROP_IMAGE_SIZE+Constants.BACKSLASH + movie.getBackdropPath();

        mCollapsingToolbar.setTitle(movie.getTitle());
        Picasso.with(this).load(imageUrlString).placeholder(R.mipmap.ic_launcher).into(movieBackdropPoster);
        movieTitle.setText(movie.getOriginalTitle());
        movieOverview.setText(movie.getOverview());
        float voteAverage = (float) movie.getVoteAverage()/2;
        movieRating.setRating(voteAverage);
        String releaseDate = "Release date: "+ movie.getReleaseDate();
        movieReleaseDate.setText(releaseDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
