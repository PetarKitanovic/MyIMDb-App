package petarkitanovic.androidkurs.omiljeniglumci;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import petarkitanovic.androidkurs.omiljeniglumci.adapters.AdapterFilmoviGlumca;
import petarkitanovic.androidkurs.omiljeniglumci.model_actor.ActorData;
import petarkitanovic.androidkurs.omiljeniglumci.model_actor.Cast;
import petarkitanovic.androidkurs.omiljeniglumci.net.MyService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.TMDB_APIKEY;
import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.IMAGEBASEURL;

public class GlumciDetails extends AppCompatActivity {

    TextView imeGlumca, bornGlumac, diedGlumac, biografijaGlumac, birthplaceGLumac;
    ImageView slikaGlumca;
    RecyclerView filmoviGlumca;
    AdapterFilmoviGlumca adapter;

    ActorData actorDetails;
    List<petarkitanovic.androidkurs.omiljeniglumci.model_actor.Cast> actorCast;
    Cast pojedinacanActorCast;

    Button btn_see_all;
    private long lastClickTime = 0;
    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalji_glumaca_activity);

        setupActionBar();
        initViews();

        id = getIntent().getIntExtra("id", 0);
        getAllActorsData(id);


        btn_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - lastClickTime >= 1000) {
                    lastClickTime = SystemClock.elapsedRealtime();

                    Intent intent = new Intent(GlumciDetails.this, ListaFilmova.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }
        });
    }

    public void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
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
                Intent intent = new Intent(GlumciDetails.this,OmiljeniFilmovi.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initViews() {

        imeGlumca = findViewById(R.id.ime_glumaca);
        slikaGlumca = findViewById(R.id.slika_glumaca);
        bornGlumac = findViewById(R.id.born_glumac);
        diedGlumac = findViewById(R.id.died_glumac);
        biografijaGlumac = findViewById(R.id.biografija_glumca);
        birthplaceGLumac = findViewById(R.id.birthplace_glumac);
        filmoviGlumca = findViewById(R.id.filmovi_glumca);

        btn_see_all = findViewById(R.id.tv_see_all_filmove_glumca);

    }

    private void getAllActorsData(int id) {
        MyService.apiInterface().getAllActorsData(id, TMDB_APIKEY, "movie_credits")
                .enqueue(new Callback<ActorData>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<ActorData> call, @NonNull Response<ActorData> response) {

                        actorDetails = response.body();


                        if (actorDetails != null) {
                            imeGlumca.setText(actorDetails.getName());


                            if (actorDetails.getBiography().isEmpty()) {
                                biografijaGlumac.setVisibility(View.GONE);

                            } else {
                                biografijaGlumac.setVisibility(View.VISIBLE);
                                biografijaGlumac.setText(actorDetails.getBiography());
                                biografijaGlumac.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (SystemClock.elapsedRealtime() - lastClickTime >= 1000) {
                                            lastClickTime = SystemClock.elapsedRealtime();

                                            Intent intent = new Intent(GlumciDetails.this, Biografija.class);
                                            intent.putExtra("biografija", actorDetails.getBiography());
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }

                            if (actorDetails.getPlaceOfBirth() != null) {
                                birthplaceGLumac.setVisibility(View.VISIBLE);
                                birthplaceGLumac.setText("in " + actorDetails.getPlaceOfBirth());
                            }
                            if (actorDetails.getBirthday() != null) {
                                bornGlumac.setVisibility(View.VISIBLE);
                                bornGlumac.setText("Born: " + actorDetails.getBirthday());
                            }
                            if (actorDetails.getDeathday() != null) {
                                diedGlumac.setVisibility(View.VISIBLE);
                                diedGlumac.setText("Died: " + actorDetails.getDeathday());
                            }


                            if (actorDetails.getProfilePath() == null) {
                                slikaGlumca.setImageDrawable(getDrawable(R.drawable.glava_silueta));
                            } else {
                                Glide.with(GlumciDetails.this)
                                        .load(IMAGEBASEURL + actorDetails.getProfilePath())
                                        .into(slikaGlumca);
                            }

                            actorCast = actorDetails.getMovieCredits().getCast();

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GlumciDetails.this, LinearLayoutManager.HORIZONTAL, false);
                            filmoviGlumca.setLayoutManager(linearLayoutManager);

                            for (int j = 0; j < actorCast.size() - 1; j++) {
                                for (int k = j + 1; k < actorCast.size(); k++) {
                                    if (actorCast.get(j).getPopularity() < actorCast.get(k).getPopularity()) {
                                        pojedinacanActorCast = actorCast.get(j);
                                        actorCast.set(j, actorCast.get(k));
                                        actorCast.set(k, pojedinacanActorCast);

                                    }
                                }
                            }

                            adapter = new AdapterFilmoviGlumca(GlumciDetails.this, actorCast);

                            filmoviGlumca.setAdapter(adapter);

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ActorData> call, @NonNull Throwable t) {
                        Toast.makeText(GlumciDetails.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
