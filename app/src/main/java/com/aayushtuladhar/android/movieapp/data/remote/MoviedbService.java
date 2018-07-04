package com.aayushtuladhar.android.movieapp.data.remote;


import com.aayushtuladhar.android.movieapp.model.MovieResponse;
import com.aayushtuladhar.android.movieapp.model.MovieReviewsResponse;
import com.aayushtuladhar.android.movieapp.model.MovieVideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviedbService {

    @GET("3/movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<MovieVideosResponse> getVideosForMovie(@Path("id")int id, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<MovieReviewsResponse> getReviewsForMovie(@Path("id")int id, @Query("api_key") String apiKey);

}