package petarkitanovic.androidkurs.omiljeniglumci;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import petarkitanovic.androidkurs.omiljeniglumci.adapters.AdapterFilmovi;
import petarkitanovic.androidkurs.omiljeniglumci.adapters.AdapterSearch;
import petarkitanovic.androidkurs.omiljeniglumci.adapters.AdapterTopMovies;
import petarkitanovic.androidkurs.omiljeniglumci.adapters.AdapterUpcomingMovies;
import petarkitanovic.androidkurs.omiljeniglumci.models.MoviesResponse;
import petarkitanovic.androidkurs.omiljeniglumci.models.ResultsMovieItem;
import petarkitanovic.androidkurs.omiljeniglumci.net.MyService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static petarkitanovic.androidkurs.omiljeniglumci.net.MyServiceContract.TMDB_APIKEY;

public class MainActivity extends AppCompatActivity implements AdapterSearch.OnItemClickListener, AdapterFilmovi.OnItemClickListener {

    private RecyclerView popularRecycler, topRatedRecycler, upcomingRecycler, searchRecycler;
    private ScrollView glavni_layout;
    private LinearLayoutManager linearLayoutManager;
    private AdapterFilmovi adapterFilmovi;
    private AdapterSearch adapterSearch;
    private AdapterTopMovies adapterTopMovies;
    private AdapterUpcomingMovies adapterUpcomingMovies;

    private List<ResultsMovieItem> searchedMovieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        getPopularMovies();
        getTopRatedMovies();
        getUpcomingMovies();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);


        MenuItem searchViewItem = menu.findItem(R.id.search);

        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Enter movie name");


        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                glavni_layout.setVisibility(View.GONE);
                searchRecycler.setVisibility(View.VISIBLE);
                searchMovieByName(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                glavni_layout.setVisibility(View.GONE);
                searchRecycler.setVisibility(View.VISIBLE);
                searchMovieByName(newText);

                return false;
            }
        });

        searchView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchRecycler.setVisibility(View.GONE);

                glavni_layout.setVisibility(View.VISIBLE);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_whishlist) {
            Intent intent = new Intent(MainActivity.this, OmiljeniFilmovi.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void initViews() {

        glavni_layout = findViewById(R.id.glavni_layout);
        popularRecycler = findViewById(R.id.recyclerViewPopularMovies);
        topRatedRecycler = findViewById(R.id.recyclerViewTopRatedMovies);
        upcomingRecycler = findViewById(R.id.recyclerViewUpcomingMovies);
        searchRecycler = findViewById(R.id.search_movies);

        searchRecycler.addItemDecoration(new DividerKlasa(popularRecycler.getContext(), DividerKlasa.VERTICAL));
        searchRecycler.setHasFixedSize(true);
        searchRecycler.setItemViewCacheSize(20);

        popularRecycler.setHasFixedSize(true);
        popularRecycler.setItemViewCacheSize(20);

        topRatedRecycler.setHasFixedSize(true);
        topRatedRecycler.setItemViewCacheSize(20);

        upcomingRecycler.setHasFixedSize(true);
        upcomingRecycler.setItemViewCacheSize(20);
    }

    private void getPopularMovies() {
        MyService.apiInterface().getPopularMovies(TMDB_APIKEY, "GB")
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.code() == 200) {
                            try {

                                if (response.body() != null) {
                                    searchedMovieList = response.body().getResults();


                                    Log.v("onResponse", searchedMovieList.size() + " Movies");

                                    linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    popularRecycler.setLayoutManager(linearLayoutManager);

                                    adapterFilmovi = new AdapterFilmovi(MainActivity.this, searchedMovieList, MainActivity.this);

                                    popularRecycler.setAdapter(adapterFilmovi);

                                }
                            } catch (NullPointerException e) {
                                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.v("response.code", " Greska sa serverom");
                            Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void getTopRatedMovies() {
        MyService.apiInterface().getTopRatedMovies(TMDB_APIKEY, "GB")
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.code() == 200) {
                            try {

                                if (response.body() != null) {
                                    searchedMovieList = response.body().getResults();


                                    Log.v("onResponse", searchedMovieList.size() + " Movies");

                                    linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    topRatedRecycler.setLayoutManager(linearLayoutManager);

                                    adapterTopMovies = new AdapterTopMovies(MainActivity.this, searchedMovieList);

                                    topRatedRecycler.setAdapter(adapterTopMovies);
                                }

                            } catch (NullPointerException e) {
                                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.v("response.code", " Greska sa serverom");
                            Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void getUpcomingMovies() {
        MyService.apiInterface().getUpcomingMovies(TMDB_APIKEY, "US")
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.code() == 200) {
                            try {

                                if (response.body() != null) {
                                    searchedMovieList = response.body().getResults();


                                    Log.v("onResponse", searchedMovieList.size() + " Movies");

                                    linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    upcomingRecycler.setLayoutManager(linearLayoutManager);

                                    adapterUpcomingMovies = new AdapterUpcomingMovies(MainActivity.this, searchedMovieList);

                                    upcomingRecycler.setAdapter(adapterUpcomingMovies);
                                }

                            } catch (NullPointerException e) {
                                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        } else {

                            Log.v("response.code", " Greska sa serverom");
                            Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void searchMovieByName(String query) {
        MyService.apiInterface().searchForMovies(query, TMDB_APIKEY)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.code() == 200) {
                            try {

                                if (response.body() != null) {
                                    searchedMovieList = response.body().getResults();


                                    Log.v("onResponse", searchedMovieList.size() + " Movies");

                                    linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                                    searchRecycler.setLayoutManager(linearLayoutManager);

                                    adapterSearch = new AdapterSearch(MainActivity.this, searchedMovieList, MainActivity.this);

                                    searchRecycler.setAdapter(adapterSearch);
                                }

                            } catch (NullPointerException e) {
                                Toast.makeText(MainActivity.this, "Ne postoji film/serija sa tim nazivom", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Log.v("response.code", " Greska sa serverom");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        ResultsMovieItem resultsMovieItem = adapterFilmovi.get(position);

        Intent i = new Intent(MainActivity.this, DetaljiFilma.class);
        i.putExtra("id", resultsMovieItem.getId());
        startActivity(i);

    }

    @Override
    public void onSearchMovieClick(int position) {
        ResultsMovieItem resultsMovieItem = adapterSearch.get(position);

        Intent i = new Intent(MainActivity.this, DetaljiFilma.class);
        i.putExtra("id", resultsMovieItem.getId());
        startActivity(i);
    }
}
