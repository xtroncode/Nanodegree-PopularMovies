package www.meeteor.me.popularmovies.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
        setUpFragment(savedInstanceState);
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

    private void setUpFragment(Bundle savedInstanceState) {
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
