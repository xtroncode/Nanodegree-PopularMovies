package www.meeteor.me.popularmovies.data;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import www.meeteor.me.popularmovies.util.Constants;

/**
 * Created by meet on 1/4/16.
 */
public class GetMoviesTask extends AsyncTask<Integer, Void, JSONArray> {

    private WeakReference<OnExecutionComplete> listener;
    private String SORT_PARAMETER;
    public GetMoviesTask(OnExecutionComplete listener, String SORT_PARAMETER) {
        super();
        this.listener = new WeakReference<>(listener);
        this.SORT_PARAMETER = SORT_PARAMETER;
    }

    @Override
    protected JSONArray doInBackground(Integer... params) {

        URL url;
        try {
            url = new URL("http://api.themoviedb.org/3/movie/"+SORT_PARAMETER+"?api_key="+ Constants.MOVIEDB_API_KEY+"&page="+params[0].toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                String response = "";
                while ((line = rd.readLine()) != null) {
                    response += line;
                }
                JSONObject rObj = new JSONObject(response);

                return rObj.getJSONArray("results");
            }

        }  catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        ArrayList<Movie> arrayList = new ArrayList<>();
        if(jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    arrayList.add(new Movie(jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listener.get().handleAsyncResponse(arrayList);
        }

    }

    public interface OnExecutionComplete{
         void handleAsyncResponse(ArrayList<Movie> responseMovieList);
    }
}
