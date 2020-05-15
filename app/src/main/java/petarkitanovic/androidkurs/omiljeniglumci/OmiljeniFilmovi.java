package petarkitanovic.androidkurs.omiljeniglumci;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import petarkitanovic.androidkurs.omiljeniglumci.adapters.AdapterOmiljenih;
import petarkitanovic.androidkurs.omiljeniglumci.database.DatabaseHelper;
import petarkitanovic.androidkurs.omiljeniglumci.database.FavoriteMovies;

public class OmiljeniFilmovi extends AppCompatActivity {

    private RecyclerView lista_omiljenih;
    private DatabaseHelper databaseHelper;
    private AdapterOmiljenih adapterOmiljenih;
    private List<FavoriteMovies> favoriteMoviesList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.omiljeni_layout);

        setupActionBar();

        Toast.makeText(this, "Swipe right to delete", Toast.LENGTH_SHORT).show();

        lista_omiljenih = findViewById(R.id.omiljeni_lista);
        lista_omiljenih.addItemDecoration(new DividerKlasa(lista_omiljenih.getContext(), DividerKlasa.VERTICAL));
        lista_omiljenih.setHasFixedSize(true);
        lista_omiljenih.setItemViewCacheSize(20);

        inicalizacijaRecycler();

    }

    private void inicalizacijaRecycler() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        lista_omiljenih.setLayoutManager(layoutManager);

        try {

            favoriteMoviesList = getDataBaseHelper().getFilmoviDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TextView isEmpty = findViewById(R.id.watchlist_empty);
        if (favoriteMoviesList.isEmpty()) {
            isEmpty.setVisibility(View.VISIBLE);
        }


        adapterOmiljenih = new AdapterOmiljenih(this, favoriteMoviesList);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(lista_omiljenih);
        lista_omiljenih.setAdapter(adapterOmiljenih);
    }


    public void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Watchlist");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn);

            actionBar.setHomeButtonEnabled(true);
            actionBar.show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

    }

    public DatabaseHelper getDataBaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onResume() {
        super.onResume();

        inicalizacijaRecycler();

    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            FavoriteMovies filmZaBrisanje = adapterOmiljenih.get(viewHolder.getAdapterPosition());
            try {
                getDataBaseHelper().getFilmoviDao().deleteById(filmZaBrisanje.getmDbID());
                adapterOmiljenih.notifyDataSetChanged();

                inicalizacijaRecycler();


            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                    .addBackgroundColor(ContextCompat.getColor(OmiljeniFilmovi.this, R.color.colorDelete))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}
