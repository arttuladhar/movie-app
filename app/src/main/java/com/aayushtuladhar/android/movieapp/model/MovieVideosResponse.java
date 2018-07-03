package com.aayushtuladhar.android.movieapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieVideosResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieVideo> movieVideos = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieVideo> getMovieVideos() {
        return movieVideos;
    }

    public void setMovieVideos(List<MovieVideo> movieVideos) {
        this.movieVideos = movieVideos;
    }
}