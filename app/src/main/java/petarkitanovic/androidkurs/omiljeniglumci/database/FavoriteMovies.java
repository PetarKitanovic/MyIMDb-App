package petarkitanovic.androidkurs.omiljeniglumci.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = FavoriteMovies.TABLE_NAME)
public class FavoriteMovies {

    static final String TABLE_NAME = "movies";
    private static final String FIELD_DB_ID = "dbId";
    private static final String FIELD_ID = "id";
    private static final String FIELD_MOVIE_NAME = "name";
    private static final String FIELD_DATE = "date";
    private static final String FIELD_RUNTIME = "runtime";
    private static final String FIELD_RATING = "rating";
    private static final String FIELD_IMAGE = "image";

    @DatabaseField(columnName = FIELD_DB_ID, generatedId = true)
    private int mDbID;

    @DatabaseField(columnName = FIELD_ID)
    private int mId;

    @DatabaseField(columnName = FIELD_MOVIE_NAME)
    private String mMovieName;

    @DatabaseField(columnName = FIELD_DATE)
    private String mDate;

    @DatabaseField(columnName = FIELD_RUNTIME)
    private int mRuntime;

    @DatabaseField(columnName = FIELD_RATING)
    private String mRating;

    @DatabaseField(columnName = FIELD_IMAGE)
    private String mImage;

    public FavoriteMovies() {
    }

    public int getmDbID() {
        return mDbID;
    }

    public void setmDbID(int mDbID) {
        this.mDbID = mDbID;
    }


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmMovieName() {
        return mMovieName;
    }

    public void setmMovieName(String mMovieName) {
        this.mMovieName = mMovieName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public int getmRuntime() {
        return mRuntime;
    }

    public void setmRuntime(int mRuntime) {
        this.mRuntime = mRuntime;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }


}
