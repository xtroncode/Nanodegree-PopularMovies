package www.meeteor.me.popularmovies.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Meet on 28-08-2016.
 */
public class Review {

    private String id;
    private String author;
    private String content;

    public Review(JSONObject review) throws JSONException {
        this.id = review.getString("id");
        this.author = review.getString("author");
        this.content = review.getString("content");
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    public void setContent(String content) {
        this.content = content;
    }


}
