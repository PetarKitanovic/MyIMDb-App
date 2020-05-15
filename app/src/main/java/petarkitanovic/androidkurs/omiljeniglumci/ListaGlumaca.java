package petarkitanovic.androidkurs.omiljeniglumci;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import petarkitanovic.androidkurs.omiljeniglumci.adapters.AdapterSviGlumci;
import petarkitanovic.androidkurs.omiljeniglumci.models.CastItem;
import petarkitanovic.androidkurs.omiljeniglumci.models.MovieCrewResponse;
import petarkitanovic.androidkurs.omiljeniglumci.net.MyService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.TMDB_APIKEY;

public class ListaGlumaca extends AppCompatActivity {

    private RecyclerView rvListaGlumaca;
    private MovieCrewResponse movieCrewResponse;
    private AdapterSviGlumci adapter;

    private List<CastItem> castItemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_glumaca_activity);

        setupActionBar();

        int id = getIntent().getIntExtra("id", 0);
        getMovieCast(id);

        rvListaGlumaca = findViewById(R.id.rv_lista_svih_glumaca);
        rvListaGlumaca.addItemDecoration(new DividerKlasa(rvListaGlumaca.getContext(), DividerKlasa.VERTICAL));
        rvListaGlumaca.setHasFixedSize(true);
        rvListaGlumaca.setItemViewCacheSize(20);


    }

    public void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Full Cast");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn);

            actionBar.setHomeButtonEnabled(true);
            actionBar.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.movie_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_whishlist:
                Intent intent = new Intent(ListaGlumaca.this, OmiljeniFilmovi.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void getMovieCast(int id) {
        MyService.apiInterface().getMoviesCast(id, TMDB_APIKEY)
                .enqueue(new Callback<MovieCrewResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieCrewResponse> call, @NonNull Response<MovieCrewResponse> response) {
                        if (response.code() == 200) {
                            try {

                                movieCrewResponse = response.body();

                                if (movieCrewResponse != null) {
                                    castItemList = movieCrewResponse.getCast();


                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListaGlumaca.this);
                                    rvListaGlumaca.setLayoutManager(linearLayoutManager);


                                    adapter = new AdapterSviGlumci(ListaGlumaca.this, castItemList);

                                    rvListaGlumaca.setAdapter(adapter);
                                }

                            } catch (NullPointerException e) {
                                Toast.makeText(ListaGlumaca.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Log.v("response.code", " Greska sa serverom");
                            Toast.makeText(ListaGlumaca.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieCrewResponse> call, @NonNull Throwable t) {
//                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(ListaGlumaca.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
