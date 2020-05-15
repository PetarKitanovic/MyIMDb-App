package petarkitanovic.androidkurs.omiljeniglumci.net;


import java.util.Map;

import petarkitanovic.androidkurs.omiljeniglumci.model_omdb.Detalji;
import petarkitanovic.androidkurs.omiljeniglumci.model_actor.ActorData;
import petarkitanovic.androidkurs.omiljeniglumci.model_movie.MovieDetails;
import petarkitanovic.androidkurs.omiljeniglumci.models.ActorDetailsResponse;
import petarkitanovic.androidkurs.omiljeniglumci.models.CastResult;
import petarkitanovic.androidkurs.omiljeniglumci.models.MovieCrewResponse;
import petarkitanovic.androidkurs.omiljeniglumci.models.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MyApiEndpointInterface {


    @GET("movie/{movie_id}")
    Call<MovieDetails> getAllMoviesData(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("append_to_response") String append_to_response, @Query("include_image_language") String include_image_language);

    @GET("person/{person_id}")
    Call<ActorData> getAllActorsData(@Path("person_id") int actor_id, @Query("api_key") String apikey, @Query("append_to_response") String append_to_response);


    // Search movies TMDB api
    @GET("search/movie")
    Call<MoviesResponse> searchForMovies(@Query("query") String query, @Query("api_key") String apiKey);


    // Popular, TopRated, UpComing movies TMDB api
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("region") String region);

    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(@Query("api_key") String apiKey, @Query("region") String region);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("region") String region);


    @GET("movie/{movie_id}/credits")
    Call<MovieCrewResponse> getMoviesCast(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

    @GET("person/{person_id}")
    Call<ActorDetailsResponse> getActorData(@Path("person_id") int actor_id, @Query("api_key") String apikey);


    @GET("person/{person_id}/movie_credits")
    Call<CastResult> getActorMovies(@Path("person_id") int actor_id, @Query("api_key") String apikey);


    //OMDB api
    @GET("/")
    Call<Detalji> getMovieData(@QueryMap Map<String, String> options);

}
