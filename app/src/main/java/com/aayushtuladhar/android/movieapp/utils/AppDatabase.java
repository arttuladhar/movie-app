package com.aayushtuladhar.android.movieapp.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.aayushtuladhar.android.movieapp.dao.FavoriteMovieDao;
import com.aayushtuladhar.android.movieapp.data.FavoriteMovie;

@Database(entities = {FavoriteMovie.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase INSTANCE;

    public abstract FavoriteMovieDao favoriteMovieDao();

    public static AppDatabase getAppDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "movie-app-db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
