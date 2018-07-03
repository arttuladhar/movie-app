package com.aayushtuladhar.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.aayushtuladhar.android.movieapp.adapter.MoviesAdapter;
import com.aayushtuladhar.android.movieapp.data.Select;
import com.aayushtuladhar.android.movieapp.model.Movie;
import com.aayushtuladhar.android.movieapp.model.MovieResponse;
import com.aayushtuladhar.android.movieapp.utils.MovieDbClientUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Register onSharedPreferenceChangeListener to Activity
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Select sort = getSortFromPreferences();
        getMoviesfromApi(this, sort);
    }

    private Select getSortFromPreferences(){
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);

        final String settingSortKey = getString(R.string.settings_sort_by_key);

        String sortBy = sharedPreference.getString(settingSortKey, "");
        switch (sortBy){
            case "Most Popular":
                Log.i(TAG, "Most Popular Selected");
                return Select.MOST_POPULAR;
            case "Highest Rated":
                Log.i(TAG, "Top Rated Movies Selected");
                return Select.TOP_RATED;
        }
        Log.e(TAG, "For Unknown Preferences; return MOST_POPULAR");
        return Select.MOST_POPULAR;
    }

    private void getMoviesfromApi(final Context context, Select select){
        Call<MovieResponse> popularMoviesResponse = MovieDbClientUtils.getPopularMovies(select);

        popularMoviesResponse.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                if (response.isSuccessful()){
                    final List<Movie> popularMovies = response.body().getMovies();

                    MoviesAdapter moviesAdapter = new MoviesAdapter(context, popularMovies);

                    GridView gridView = findViewById(R.id.popular_movies_listview);
                    gridView.setAdapter(moviesAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            launchMovieDetails(popularMovies.get(position));
                        }
                    });
                }else {
                    Log.wtf(TAG, "Error perfoming API call. Make sure API Key is Valid");
                    closeOnError();
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.wtf(TAG, "Error perfoming API call. Make sure API Key is Valid");
                closeOnError();
            }
        });
    }

    private void launchMovieDetails(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.preference, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.preferences){
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeOnError() {
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.settings_sort_by_key))){
            Select sort = getSortFromPreferences();
            //Refresh List
            getMoviesfromApi(this, sort);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreference.unregisterOnSharedPreferenceChangeListener(this);
    }
}
