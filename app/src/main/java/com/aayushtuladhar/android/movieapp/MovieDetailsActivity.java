package com.aayushtuladhar.android.movieapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aayushtuladhar.android.movieapp.databinding.ActivityMovieDetailsBinding;
import com.aayushtuladhar.android.movieapp.model.Movie;
import com.aayushtuladhar.android.movieapp.utils.MovieDbClientUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE = "MOVIE";
    public static final String TAG = MovieDetailsActivity.class.getCanonicalName();

    ActivityMovieDetailsBinding activityMovieDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        activityMovieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        Movie movie = (Movie) intent.getSerializableExtra(MOVIE);
        populateView(movie);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateView(Movie movie){
        Log.d(TAG, "Populating View Elements for Movie Details");

        // Load Image Using Picasso
        ImageView backdropImg = findViewById(R.id.imgMovieBackdrop);
        String backdropUrlPath = MovieDbClientUtils.POSTER_BASEURL + movie.getPosterPath();
        Picasso.with(this).load(backdropUrlPath).into(backdropImg);

        activityMovieDetailsBinding.textViewMovieTitle.setText(setNAIfEmpty(movie.getTitle()));
        activityMovieDetailsBinding.textViewReleaseDate.setText(setNAIfEmpty(movie.getReleaseDate()));
        activityMovieDetailsBinding.textViewUserRating.setText(setNAIfEmpty(String.valueOf(movie.getVoteAverage())));
        activityMovieDetailsBinding.textViewMoviePlot.setText(setNAIfEmpty(movie.getOverview()));

    }

    private String setNAIfEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            return "N/A";
        } else {
            return str;
        }
    }

}
