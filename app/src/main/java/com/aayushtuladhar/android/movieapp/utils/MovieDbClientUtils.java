package com.aayushtuladhar.android.movieapp.utils;

import com.aayushtuladhar.android.movieapp.data.SortBy;
import com.aayushtuladhar.android.movieapp.data.remote.MoviedbService;
import com.aayushtuladhar.android.movieapp.data.remote.RetrofitClient;
import com.aayushtuladhar.android.movieapp.model.MovieResponse;

import retrofit2.Call;

public class MovieDbClientUtils {

    public static final String BASE_URL = "http://api.themoviedb.org";
    public static final String POSTER_BASEURL = "http://image.tmdb.org/t/p/w185/";
    private static final String API_KEY = "ENTER_API_KEY";

    public static Call<MovieResponse> getPopularMovies(SortBy sort){
        MoviedbService moviedbService = RetrofitClient.getClient(BASE_URL).create(MoviedbService.class);
        switch (sort){
            case MOST_POPULAR:
                return moviedbService.getPopularMovies(API_KEY);
            case TOP_RATED:
                return moviedbService.getTopRatedMovies(API_KEY);
            default:
                return moviedbService.getPopularMovies(API_KEY);
        }
    }
}
