package www.meeteor.me.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by meet on 8/6/16.
 */
public class MovieProvider extends ContentProvider {

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/movies";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/movie";
    private static final int MOVIES = 1;
    private static final int MOVIES_ID = 2;
    private static final String AUTHORITY = "www.meeteor.me.popularmovies";
    private static final String BASE_PATH = "movies";
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final SQLiteQueryBuilder sMovieQueryBuilder;
    private static final String sMovieSelection = MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";

    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, MOVIES);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIES_ID);
    }

    static {
        sMovieQueryBuilder = new SQLiteQueryBuilder();

        sMovieQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
    }

    private MovieOpenHelper movieOpenHelper;

    private Cursor getMovieByMovieID(Uri uri, String[] projection, String sortOrder) {
        String MovieID = MovieContract.MovieEntry.getMovieIDFromUri(uri);

        String[] selectionArgs = new String[]{MovieID};
        String selection;

        selection = sMovieSelection;


        return sMovieQueryBuilder.query(movieOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
    }


    @Override
    public boolean onCreate() {
        movieOpenHelper = new MovieOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sUriMatcher.match(uri);
        Cursor cur;
        switch (uriType) {
            case MOVIES:
                cur = movieOpenHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOVIES_ID:
                cur = getMovieByMovieID(uri, projection, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI :" + uri);
        }
        cur.setNotificationUri(getContext().getContentResolver(), uri);
        return cur;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return CONTENT_TYPE;
            case MOVIES_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = movieOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri retUri;
        switch (match) {
            case MOVIES:
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    retUri = MovieContract.MovieEntry.buildMovieUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row at : " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case MOVIES_ID:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case MOVIES_ID:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
