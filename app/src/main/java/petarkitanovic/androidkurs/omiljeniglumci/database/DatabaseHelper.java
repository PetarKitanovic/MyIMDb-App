package petarkitanovic.androidkurs.omiljeniglumci.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "myimdb.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<FavoriteMovies, Integer> mFilmoviDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, FavoriteMovies.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {


        try {
            TableUtils.dropTable(connectionSource, FavoriteMovies.class, true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<FavoriteMovies, Integer> getFilmoviDao() throws SQLException {
        if (mFilmoviDao == null) {
            mFilmoviDao = getDao(FavoriteMovies.class);
        }

        return mFilmoviDao;
    }

    @Override
    public void close() {
        mFilmoviDao = null;

        super.close();
    }
}
