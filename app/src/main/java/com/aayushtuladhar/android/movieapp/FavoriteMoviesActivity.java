package com.aayushtuladhar.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aayushtuladhar.android.movieapp.adapter.FavoriteMovieAdapter;
import com.aayushtuladhar.android.movieapp.data.FavoriteMovie;
import com.aayushtuladhar.android.movieapp.model.Movie;
import com.aayushtuladhar.android.movieapp.utils.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavoriteMoviesActivity extends AppCompatActivity {

    public static final String TAG = FavoriteMoviesActivity.class.getSimpleName();

    public static final String MOVIE_ID = "MOVIE_ID";
    private AppDatabase mDb;
    private List<FavoriteMovie> mFavMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        mDb = AppDatabase.getAppDatabase(this);

        new GetFavoriteAsync(this).execute();
    }

    class GetFavoriteAsync extends AsyncTask<Void, Void, List<FavoriteMovie>> {
        private Context mContext;

        public GetFavoriteAsync(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected List<FavoriteMovie> doInBackground(Void... voids) {
            return mDb.favoriteMovieDao().getAll();
        }

        @Override
        protected void onPostExecute(final List<FavoriteMovie> favoriteMovies) {
            super.onPostExecute(favoriteMovies);
            mFavMovies = favoriteMovies;
            Log.i(TAG, favoriteMovies.toString());

            FavoriteMovieAdapter favoriteMovieAdapter = new FavoriteMovieAdapter(mContext, mFavMovies);
            ListView listView = findViewById(R.id.fav_list);
            listView.setAdapter(favoriteMovieAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FavoriteMovie favoriteMovie = favoriteMovies.get(position);
                    launchMovieDetails(favoriteMovie.getId());
                }
            });


        }
    }

    private void launchMovieDetails(Integer movieId) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_ID, movieId);
        startActivity(intent);
    }
}