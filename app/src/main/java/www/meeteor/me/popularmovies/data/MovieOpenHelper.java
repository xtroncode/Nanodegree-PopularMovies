package www.meeteor.me.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by meet on 8/6/16.
 */
public class MovieOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "favmovies";

    private static final String MOVIE_TABLE_CREATE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
            MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT," +
            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE," +
            MovieContract.MovieEntry.COLUMN_TITLE + " TEXT," +
            MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT," +
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT," +
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT," +
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT," +
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT" +
            ");";


    public MovieOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(MOVIE_TABLE_CREATE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.d("Message:", "upgrade table tickets executed");
            db.execSQL(" DROP TABLE " + MovieContract.MovieEntry.TABLE_NAME + " IF EXISTS;");
            onCreate(db);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
}
