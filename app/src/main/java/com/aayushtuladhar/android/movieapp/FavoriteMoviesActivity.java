package com.aayushtuladhar.android.movieapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aayushtuladhar.android.movieapp.adapter.FavoriteMovieAdapter;
import com.aayushtuladhar.android.movieapp.data.FavoriteMovie;
import com.aayushtuladhar.android.movieapp.utils.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavoriteMoviesActivity extends AppCompatActivity {

    public static final String TAG = FavoriteMoviesActivity.class.getSimpleName();
    private AppDatabase mDb;
    private List<FavoriteMovie> mFavMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        mDb = AppDatabase.getAppDatabase(this);

        AsyncTask<Void, Void, List<FavoriteMovie>> getFavoriteAsync = new GetFavoriteAsync();
        try {
            mFavMovies = getFavoriteAsync.execute().get();
            FavoriteMovieAdapter favoriteMovieAdapter = new FavoriteMovieAdapter(this, mFavMovies);
            ListView listView = findViewById(R.id.fav_list);
            listView.setAdapter(favoriteMovieAdapter);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    class GetFavoriteAsync extends AsyncTask<Void, Void, List<FavoriteMovie>> {

        @Override
        protected List<FavoriteMovie> doInBackground(Void... voids) {
            return mDb.favoriteMovieDao().getAll();
        }

        @Override
        protected void onPostExecute(List<FavoriteMovie> favoriteMovies) {
            super.onPostExecute(favoriteMovies);
            mFavMovies = favoriteMovies;
            Log.i(TAG, favoriteMovies.toString());
        }

    }


}
