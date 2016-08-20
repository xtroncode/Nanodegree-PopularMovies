package www.meeteor.me.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by meet on 8/6/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "www.meeteor.me.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movies";


    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "Movies";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String[] MOVIE_PROJECTIONS = {
                MovieEntry.COLUMN_MOVIE_ID,
                MovieEntry.COLUMN_BACKDROP_PATH,
                MovieEntry.COLUMN_OVERVIEW,
                MovieEntry.COLUMN_POSTER_PATH,
                MovieEntry.COLUMN_RELEASE_DATE,
                MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieEntry.COLUMN_ORIGINAL_TITLE,
                MovieEntry.COLUMN_TITLE
        };
        public static final int MOVIE_ID_COl_INDEX = 0;
        public static final int POSTER_PATH_COl_INDEX = 3;
        public static final int TITLE_COl_INDEX = 7;
        public static final int OVERVIEW_COl_INDEX = 2;
        public static final int BACKDROP_PATH_COl_INDEX = 1;
        public static final int VOTE_AVERAGE_COl_INDEX = 5;
        public static final int ORIGINAL_TITLE_COl_INDEX = 6;
        public static final int RELEASE_DATE_COl_INDEX = 4;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


}
