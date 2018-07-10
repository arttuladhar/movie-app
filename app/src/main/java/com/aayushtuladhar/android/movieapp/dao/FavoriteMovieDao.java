package com.aayushtuladhar.android.movieapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.aayushtuladhar.android.movieapp.FavoriteMoviesActivity;
import com.aayushtuladhar.android.movieapp.data.FavoriteMovie;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FavoriteMovie... favoriteMovie);

    @Query("SELECT * FROM favorite_movie")
    List<FavoriteMovie> getAll();

    @Delete
    void delete(FavoriteMovie... favoriteMovie);

}
