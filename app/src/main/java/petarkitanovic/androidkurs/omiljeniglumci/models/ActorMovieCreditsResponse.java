package petarkitanovic.androidkurs.omiljeniglumci.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActorMovieCreditsResponse {

    @SerializedName("cast")
    private List<CastActorItem> cast;

    @SerializedName("id")
    private int id;

    @SerializedName("crew")
    private List<CrewItem> crew;

    public void setCast(List<CastActorItem> cast) {
        this.cast = cast;
    }

    public List<CastActorItem> getCast() {
        return cast;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCrew(List<CrewItem> crew) {
        this.crew = crew;
    }

    public List<CrewItem> getCrew() {
        return crew;
    }

    @Override
    public String toString() {
        return
                "ActorMovieCreditsResponse{" +
                        "cast = '" + cast + '\'' +
                        ",id = '" + id + '\'' +
                        ",crew = '" + crew + '\'' +
                        "}";
    }
}