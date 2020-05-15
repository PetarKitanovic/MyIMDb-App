package petarkitanovic.androidkurs.omiljeniglumci;

import android.content.Intent;
import android.os.Bundle;
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

import petarkitanovic.androidkurs.omiljeniglumci.adapters.AdapterSviFilmovi;
import petarkitanovic.androidkurs.omiljeniglumci.models.Cast;
import petarkitanovic.androidkurs.omiljeniglumci.models.CastResult;
import petarkitanovic.androidkurs.omiljeniglumci.net.MyService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.APIKEY;

public class ListaFilmova extends AppCompatActivity {

    int id;
    private RecyclerView rvListaFilmova;
    private AdapterSviFilmovi adapter;
    private List<Cast> cast;
    private Cast pojedinacanCast;
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_glumaca_activity);

        setupActionBar();


        id = getIntent().getIntExtra("id", 0);

        getGlumacFilmovi(id);

        rvListaFilmova = findViewById(R.id.rv_lista_svih_glumaca);
        rvListaFilmova.addItemDecoration(new DividerKlasa(rvListaFilmova.getContext(), DividerKlasa.VERTICAL));
        rvListaFilmova.setHasFixedSize(true);
        rvListaFilmova.setItemViewCacheSize(20);
    }

    public void setupActionBar() {
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("");
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
                Intent intent = new Intent(ListaFilmova.this, OmiljeniFilmovi.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public Integer firstFour(String godina) {
        if (godina == null) {
            return 0;
        }
        if (godina.length() > 4) {

            return Integer.parseInt(godina.substring(0, 4));
        } else {
            return 0;
        }

    }

    private void getGlumacFilmovi(int id) {
        MyService.apiInterface().getActorMovies(id, APIKEY)
                .enqueue(new Callback<CastResult>() {
                    @Override
                    public void onResponse(@NonNull Call<CastResult> call, @NonNull Response<CastResult> response) {

                        if (response.body() != null) {
                            cast = response.body().getCast();


                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListaFilmova.this);
                            rvListaFilmova.setLayoutManager(linearLayoutManager);

                            try {
                                actionBar.setTitle("All Movies " + "(" + cast.size() + ")");
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                            for (int j = 0; j < cast.size() - 1; j++) {
                                for (int k = j + 1; k < cast.size(); k++) {
                                    if (firstFour(cast.get(j).getReleaseDate()) < firstFour(cast.get(k).getReleaseDate())) {
                                        pojedinacanCast = cast.get(j);
                                        cast.set(j, cast.get(k));
                                        cast.set(k, pojedinacanCast);

                                    }
                                }
                            }

                            adapter = new AdapterSviFilmovi(ListaFilmova.this, cast);

                            rvListaFilmova.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CastResult> call, @NonNull Throwable t) {
                        Toast.makeText(ListaFilmova.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

}
