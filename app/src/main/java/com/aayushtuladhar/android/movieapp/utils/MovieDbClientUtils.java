package com.aayushtuladhar.android.movieapp.utils;

import android.util.Log;

import com.aayushtuladhar.android.movieapp.BuildConfig;
import com.aayushtuladhar.android.movieapp.data.Select;
import com.aayushtuladhar.android.movieapp.data.remote.MoviedbService;
import com.aayushtuladhar.android.movieapp.data.remote.RetrofitClient;
import com.aayushtuladhar.android.movieapp.model.MovieResponse;
import com.aayushtuladhar.android.movieapp.model.MovieReviewsResponse;
import com.aayushtuladhar.android.movieapp.model.MovieVideosResponse;

import retrofit2.Call;

public class MovieDbClientUtils {

    public static final String TAG = MovieDbClientUtils.class.getSimpleName();

    public static final String BASE_URL = "http://api.themoviedb.org";
    public static final String POSTER_BASEURL = "http://image.tmdb.org/t/p/w185/";
    private static final String API_KEY = BuildConfig.MOVIEDB_API_KEY;

    public static Call<MovieResponse> getPopularMovies(Select select){
        MoviedbService moviedbService = RetrofitClient.getClient(BASE_URL).create(MoviedbService.class);
        switch (select){
            case MOST_POPULAR:
                return moviedbService.getPopularMovies(API_KEY);
            case TOP_RATED:
                return moviedbService.getTopRatedMovies(API_KEY);
            default:
                Log.e(TAG, "getPopularMovies with Default Select");
                return moviedbService.getPopularMovies(API_KEY);
        }
    }

    public static Call<MovieVideosResponse> getVideosForMovie(Integer movieId){
        MoviedbService moviedbService = RetrofitClient.getClient(BASE_URL).create(MoviedbService.class);
        return moviedbService.getVideosForMovie(movieId, API_KEY);
    }

    public static Call<MovieReviewsResponse> getReviewsForMovie(Integer moviId){
        MoviedbService moviedbService = RetrofitClient.getClient(BASE_URL).create(MoviedbService.class);
        return moviedbService.getReviewsForMovie(moviId, API_KEY);
    }

}
