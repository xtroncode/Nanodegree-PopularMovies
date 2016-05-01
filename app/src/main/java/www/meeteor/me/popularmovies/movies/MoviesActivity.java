package www.meeteor.me.popularmovies.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import www.meeteor.me.popularmovies.R;
import www.meeteor.me.popularmovies.moviedetail.MovieDetailFragment;
/*
    TODO: Implement Network status check
    TODO: Implement Sort settings
    TODO: Implement Detail View
 */

public class MoviesActivity extends AppCompatActivity {


    private final String TAG = "MoviesActivity";

    public  boolean mIsTwoPane;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_fragment_container) != null){
            mIsTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_fragment_container, new MovieDetailFragment(), MovieDetailFragment.TAG)
                        .commit();
            }
        }
        else{
            mIsTwoPane = false;
        }

        Log.d(TAG, String.valueOf(mIsTwoPane));

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }





}
