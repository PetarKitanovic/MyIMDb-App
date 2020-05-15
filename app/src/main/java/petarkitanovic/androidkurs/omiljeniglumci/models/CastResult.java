
package petarkitanovic.androidkurs.omiljeniglumci.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CastResult {

    @SerializedName("cast")
    @Expose
    private List<Cast> cast = null;
    @SerializedName("crew")
    @Expose
    private List<Object> crew = null;
    @SerializedName("id")
    @Expose
    private Integer id;

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Object> getCrew() {
        return crew;
    }

    public void setCrew(List<Object> crew) {
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
