
package petarkitanovic.androidkurs.omiljeniglumci.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImdbId {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @SerializedName("facebook_id")
    @Expose
    private Object facebookId;
    @SerializedName("instagram_id")
    @Expose
    private Object instagramId;
    @SerializedName("twitter_id")
    @Expose
    private Object twitterId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Object getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Object facebookId) {
        this.facebookId = facebookId;
    }

    public Object getInstagramId() {
        return instagramId;
    }

    public void setInstagramId(Object instagramId) {
        this.instagramId = instagramId;
    }

    public Object getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(Object twitterId) {
        this.twitterId = twitterId;
    }

}
