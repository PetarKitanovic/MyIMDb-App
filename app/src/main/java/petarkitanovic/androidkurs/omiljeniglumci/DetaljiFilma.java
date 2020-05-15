package petarkitanovic.androidkurs.omiljeniglumci;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import petarkitanovic.androidkurs.omiljeniglumci.adapters.AdapterGlumci;
import petarkitanovic.androidkurs.omiljeniglumci.adapters.SliderPagerAdapter;
import petarkitanovic.androidkurs.omiljeniglumci.adapters.VideoAdapter;
import petarkitanovic.androidkurs.omiljeniglumci.database.DatabaseHelper;
import petarkitanovic.androidkurs.omiljeniglumci.database.FavoriteMovies;
import petarkitanovic.androidkurs.omiljeniglumci.model_movie.Backdrop;
import petarkitanovic.androidkurs.omiljeniglumci.model_movie.Cast;
import petarkitanovic.androidkurs.omiljeniglumci.model_movie.Genre;
import petarkitanovic.androidkurs.omiljeniglumci.model_movie.MovieDetails;
import petarkitanovic.androidkurs.omiljeniglumci.model_movie.Result;
import petarkitanovic.androidkurs.omiljeniglumci.model_omdb.Detalji;
import petarkitanovic.androidkurs.omiljeniglumci.net.MyService;
import petarkitanovic.androidkurs.omiljeniglumci.net.MyService2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.TMDB_APIKEY;
import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.OMDB_APIKEY;
import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.IMAGEBASEURL;

public class DetaljiFilma extends AppCompatActivity {


    private TextView nazivFilma, plotFilma, godina, runtime, direktor, pisci, imdbOcena, imdbVotes, zanr1, zanr2, zanr3, nazivFilma2;
    private ImageView slikaFilma;
    private MovieDetails movieDetails;
    private List<Cast> movieCast;
    private List<Backdrop> backdrop;
    private FrameLayout frameLayout;
    private SliderPagerAdapter adapterSlider;
    private Button favorite_btn, remove_favorite_btn, btn_see_all;

    private ViewPager sliderpager;

    private RecyclerView rvListaGlumaca, videoRecycler;
    private AdapterGlumci adapter;
    private LinearLayout linearLayout, trailersLayout, direktorLayout, topCastLayout;

    private Detalji detalji;
    private DatabaseHelper databaseHelper;
    private List<FavoriteMovies> favoriteMovies;
    private FavoriteMovies film;
    int id;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalji_filma_layout);

        setupActionBar();

        initViews();

        id = getIntent().getIntExtra("id", 0);


        getAllMoviesData(id);

        btn_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - lastClickTime >= 1000) {
                    lastClickTime = SystemClock.elapsedRealtime();

                    Intent intent = new Intent(DetaljiFilma.this, ListaGlumaca.class);
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
                Intent intent = new Intent(DetaljiFilma.this, OmiljeniFilmovi.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void addFilm(View view) {

        film = new FavoriteMovies();

        film.setmMovieName(movieDetails.getTitle());
        film.setmDate(movieDetails.getReleaseDate());
        film.setmId(movieDetails.getId());

        if (detalji != null) {
            film.setmRating(detalji.getImdbRating());
        } else {
            film.setmRating("N/A");
        }

        film.setmRuntime(movieDetails.getRuntime());
        film.setmImage(movieDetails.getPosterPath());


        try {
            getDataBaseHelper().getFilmoviDao().create(film);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        favorite_btn.setVisibility(View.GONE);
        remove_favorite_btn.setVisibility(View.VISIBLE);


        String tekstNotifikacije = film.getmMovieName() + " is added to Watchlist!";

        Toast.makeText(DetaljiFilma.this, tekstNotifikacije, Toast.LENGTH_SHORT).show();


    }

    public void removeFilm(View view) {

        try {
            favoriteMovies = getDataBaseHelper().getFilmoviDao().queryForEq("id", movieDetails.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        film = favoriteMovies.get(0);
        try {
            getDataBaseHelper().getFilmoviDao().deleteById(film.getmDbID());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        remove_favorite_btn.setVisibility(View.GONE);
        favorite_btn.setVisibility(View.VISIBLE);

        String tekstNotifikacije = film.getmMovieName() + " is removed from Watchlist!";

        Toast.makeText(DetaljiFilma.this, tekstNotifikacije, Toast.LENGTH_SHORT).show();

    }

    private void initViews() {

        nazivFilma = findViewById(R.id.nazivFilma);
        nazivFilma2 = findViewById(R.id.nazivFilma2);
        slikaFilma = findViewById(R.id.slikaFilma);
        plotFilma = findViewById(R.id.tv_plot);

        rvListaGlumaca = findViewById(R.id.rvListaGlumaca);
        rvListaGlumaca.setHasFixedSize(true);
        rvListaGlumaca.setItemViewCacheSize(20);

        btn_see_all = findViewById(R.id.tv_see_all);

        godina = findViewById(R.id.film_godina);
        runtime = findViewById(R.id.film_trajanje);
        direktor = findViewById(R.id.ime_direktora);
        pisci = findViewById(R.id.ime_pisaca);
        imdbOcena = findViewById(R.id.imdb_ocena);
        imdbVotes = findViewById(R.id.imdb_votes);
        zanr1 = findViewById(R.id.film_zanr1);
        zanr2 = findViewById(R.id.film_zanr2);
        zanr3 = findViewById(R.id.film_zanr3);

        linearLayout = findViewById(R.id.gridLayout);

        sliderpager = findViewById(R.id.slider_pager);
        frameLayout = findViewById(R.id.movie_frame);


        videoRecycler = findViewById(R.id.video_recycler);
        trailersLayout = findViewById(R.id.trailers_layout);
        direktorLayout = findViewById(R.id.direktor_layout);
        topCastLayout = findViewById(R.id.top_cast_layout);

        favorite_btn = findViewById(R.id.favorite_btn);
        remove_favorite_btn = findViewById(R.id.delete_favorite_btn);
    }

    public String firstFour(String godina) {
        return godina.length() < 4 ? godina : godina.substring(0, 4);
    }

    public void getGenre(List<Genre> zandrovi) {
        try {
            if (zandrovi.size() >= 3) {
                zanr1.setText(zandrovi.get(0).getName());
                zanr2.setText(zandrovi.get(1).getName());
                zanr3.setText(zandrovi.get(2).getName());
            }
            if (zandrovi.size() == 2) {
                zanr1.setText(zandrovi.get(0).getName());
                zanr2.setText(zandrovi.get(1).getName());
            }
            if (zandrovi.size() == 1) {
                zanr1.setText(zandrovi.get(0).getName());
            }
            if (zandrovi.size() == 0) {
                zanr1.setVisibility(View.GONE);
                zanr2.setVisibility(View.GONE);
                zanr3.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getMovieData(final String imbd_id) {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("apikey", OMDB_APIKEY);
        queryParams.put("i", imbd_id);

        MyService2.apiInterface().getMovieData(queryParams)
                .enqueue(new Callback<Detalji>() {
                    public void onResponse(@NonNull Call<Detalji> call, @NonNull Response<Detalji> response) {
                        if (response.code() == 200) {
                            Log.d("REZ", "200");

                            detalji = response.body();
                            if (detalji != null) {

                                direktor.setText(detalji.getDirector());
                                pisci.setText(detalji.getWriter());

                                if (detalji.getImdbRating() == null) {
                                    linearLayout.setVisibility(View.GONE);
                                } else if (detalji.getImdbRating().equals("N/A")) {
                                    linearLayout.setVisibility(View.GONE);
                                } else {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    imdbOcena.setText(detalji.getImdbRating());
                                    imdbVotes.setText(detalji.getImdbVotes());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Detalji> call, @NonNull Throwable t) {
                        direktorLayout.setVisibility(View.GONE);
                    }
                });

    }


    private void getAllMoviesData(int id) {
        MyService.apiInterface().getAllMoviesData(id, TMDB_APIKEY, "credits,images,videos", "en,null")
                .enqueue(new Callback<MovieDetails>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                        if (response.code() == 200) {
                            try {
                                movieDetails = response.body();

                                try {
                                    favoriteMovies = getDataBaseHelper().getFilmoviDao().queryForEq("id", movieDetails.getId());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                if (favoriteMovies.isEmpty()) {
                                    favorite_btn.setVisibility(View.VISIBLE);
                                    remove_favorite_btn.setVisibility(View.GONE);
                                } else {
                                    remove_favorite_btn.setVisibility(View.VISIBLE);
                                    favorite_btn.setVisibility(View.GONE);
                                }

                                getMovieData(movieDetails.getImdbId());

                                getGenre(movieDetails.getGenres());

                                runtime.setText(movieDetails.getRuntime() + "min");
                                nazivFilma.setText(movieDetails.getTitle());
                                nazivFilma2.setText(movieDetails.getTitle());


                                if (movieDetails.getOverview().isEmpty()) {
                                    plotFilma.setVisibility(View.GONE);

                                } else {
                                    plotFilma.setVisibility(View.VISIBLE);
                                    plotFilma.setText(movieDetails.getOverview());
                                    plotFilma.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (SystemClock.elapsedRealtime() - lastClickTime >= 1000) {
                                                lastClickTime = SystemClock.elapsedRealtime();

                                                Intent intent = new Intent(DetaljiFilma.this, PlotFilma.class);
                                                intent.putExtra("plot", movieDetails.getOverview());
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }

                                godina.setText(firstFour(movieDetails.getReleaseDate()));

                                if (movieDetails.getPosterPath() == null) {
                                    slikaFilma.setImageDrawable(getDrawable(R.drawable.ic_image_white_24dp));
                                    slikaFilma.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                } else {
                                    Glide.with(DetaljiFilma.this)
                                            .load(IMAGEBASEURL + movieDetails.getPosterPath())
                                            .into(slikaFilma);
                                }


                                movieCast = movieDetails.getCredits().getCast();

                                if (movieCast.isEmpty()) {
                                    topCastLayout.setVisibility(View.GONE);
                                }

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetaljiFilma.this, LinearLayoutManager.HORIZONTAL, false);
                                rvListaGlumaca.setLayoutManager(linearLayoutManager);

                                adapter = new AdapterGlumci(DetaljiFilma.this, movieCast);

                                rvListaGlumaca.setAdapter(adapter);

                                if (movieDetails.getImages().getBackdrops().isEmpty()) {
                                    frameLayout.setVisibility(View.GONE);
                                    nazivFilma2.setVisibility(View.VISIBLE);
                                } else {
                                    backdrop = movieDetails.getImages().getBackdrops();

                                    adapterSlider = new SliderPagerAdapter(DetaljiFilma.this, backdrop);
                                    sliderpager.setAdapter(adapterSlider);

                                    Timer timer = new Timer();
                                    timer.scheduleAtFixedRate(new DetaljiFilma.SliderTimer(), 4000, 6000);
                                }


                                List<Result> videoResult = movieDetails.getVideos().getResults();
                                List<Result> videoResult2 = new ArrayList<>();

                                for (int i = 0; i < videoResult.size(); i++) {
                                    if (videoResult.get(i).getType().equals("Trailer")) {
                                        videoResult2.add(videoResult.get(i));
                                        if (videoResult.isEmpty()) {
                                            trailersLayout.setVisibility(View.GONE);
                                        } else {
                                            trailersLayout.setVisibility(View.VISIBLE);

                                            videoRecycler.setHasFixedSize(true);
                                            videoRecycler.setLayoutManager(new LinearLayoutManager(DetaljiFilma.this, LinearLayoutManager.HORIZONTAL, false));

                                            VideoAdapter videoAdapter = new VideoAdapter(DetaljiFilma.this, videoResult2);

                                            videoRecycler.setAdapter(videoAdapter);
                                        }

                                    }
                                }


                            } catch (NullPointerException e) {
//                                Toast.makeText(DetaljiFilma.this, "Ne postoji film/serija sa tim nazivom", Toast.LENGTH_SHORT).show();
                                Toast.makeText(DetaljiFilma.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.v("response.code", " Greska sa serverom");
//                            Toast.makeText(DetaljiFilma.this, "No information about the movie", Toast.LENGTH_SHORT).show();
                            Toast.makeText(DetaljiFilma.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieDetails> call, @NonNull Throwable t) {
                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(DetaljiFilma.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public DatabaseHelper getDataBaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            favoriteMovies = getDataBaseHelper().getFilmoviDao().queryForEq("id", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (favoriteMovies.isEmpty()) {
            favorite_btn.setVisibility(View.VISIBLE);
            remove_favorite_btn.setVisibility(View.GONE);
        } else {
            remove_favorite_btn.setVisibility(View.VISIBLE);
            favorite_btn.setVisibility(View.GONE);
        }
    }

    class SliderTimer extends TimerTask {

        int getCount() {
            if (backdrop.size() > 10) {
                return 10;
            } else {
                return backdrop.size();
            }
        }

        @Override
        public void run() {


            DetaljiFilma.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderpager.getCurrentItem() < getCount() - 1) {
                        sliderpager.setCurrentItem(sliderpager.getCurrentItem() + 1);
                    } else
                        sliderpager.setCurrentItem(0);
                }
            });


        }
    }
}
