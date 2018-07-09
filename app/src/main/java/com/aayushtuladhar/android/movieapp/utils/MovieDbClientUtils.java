package com.aayushtuladhar.android.movieapp.utils;

import android.util.Log;

import com.aayushtuladhar.android.movieapp.BuildConfig;
import com.aayushtuladhar.android.movieapp.data.Select;
import com.aayushtuladhar.android.movieapp.data.remote.MoviedbService;
import com.aayushtuladhar.android.movieapp.data.remote.RetrofitClient;
import com.aayushtuladhar.android.movieapp.model.Movie;
import com.aayushtuladhar.android.movieapp.model.MovieResponse;
import com.aayushtuladhar.android.movieapp.model.MovieReviewsResponse;
import com.aayushtuladhar.android.movieapp.model.MovieVideosResponse;

import retrofit2.Call;

public class MovieDbClientUtils {

    static final String TAG = MovieDbClientUtils.class.getSimpleName();
    static final String BASE_URL = "http://api.themoviedb.org";

    public static final String POSTER_BASEURL = "http://image.tmdb.org/t/p/w185/";

    private static final String API_KEY = BuildConfig.MOVIEDB_API_KEY;

    public static Call<MovieResponse> getPopularMovies(Select select){
        MoviedbService movieDbService = RetrofitClient.getClient(BASE_URL).create(MoviedbService.class);
        switch (select){
            case MOST_POPULAR:
                return movieDbService.getPopularMovies(API_KEY);
            case TOP_RATED:
                return movieDbService.getTopRatedMovies(API_KEY);
            default:
                Log.e(TAG, "getPopularMovies with Default Select");
                return movieDbService.getPopularMovies(API_KEY);
        }
    }

    public static Call<MovieVideosResponse> getVideosForMovie(Integer movieId){
        MoviedbService movieDbService = RetrofitClient.getClient(BASE_URL).create(MoviedbService.class);
        return movieDbService.getVideosForMovie(movieId, API_KEY);
    }

    public static Call<MovieReviewsResponse> getReviewsForMovie(Integer movieId){
        MoviedbService movieDbService = RetrofitClient.getClient(BASE_URL).create(MoviedbService.class);
        return movieDbService.getReviewsForMovie(movieId, API_KEY);
    }

    public static Call<Movie> getMovieForId(Integer movieId){
        MoviedbService movieDbService = RetrofitClient.getClient(BASE_URL).create(MoviedbService.class);
        return movieDbService.getMovieForId(movieId, API_KEY);
    }

}
