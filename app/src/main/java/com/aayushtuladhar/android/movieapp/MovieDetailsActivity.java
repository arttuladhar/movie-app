package com.aayushtuladhar.android.movieapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aayushtuladhar.android.movieapp.databinding.ActivityMovieDetailsBinding;
import com.aayushtuladhar.android.movieapp.model.Movie;
import com.aayushtuladhar.android.movieapp.model.MovieVideo;
import com.aayushtuladhar.android.movieapp.model.MovieVideosResponse;
import com.aayushtuladhar.android.movieapp.utils.MovieDbClientUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity{

    public static final String MOVIE = "MOVIE";
    private static final String TAG = MovieDetailsActivity.class.getCanonicalName();
    private ActivityMovieDetailsBinding activityMovieDetailsBinding;


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

        // Make additional Calls for Movie Reviews and Trailer
        getTrailerForMovie(movie.getId());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void getTrailerForMovie(Integer id){

        Call<MovieVideosResponse> videosForMovie = MovieDbClientUtils.getVideosForMovie(id);
        videosForMovie.enqueue(new Callback<MovieVideosResponse>() {
            @Override
            public void onResponse(Call<MovieVideosResponse> call, Response<MovieVideosResponse> response) {

                if (response.isSuccessful()){
                    final List<MovieVideo> videos = response.body().getMovieVideos();
                    addTrailersForMovie(videos);

                } else{
                    Log.e(TAG, "Unsuccessful Response");
                }

            }

            @Override
            public void onFailure(Call<MovieVideosResponse> call, Throwable t) {
                Log.e(TAG, "Error getting Trailers for Movies");
                closeOnError();

            }
        });
    }

    void addTrailersForMovie(final List<MovieVideo> videos){
        if (videos.size() >= 2){
            activityMovieDetailsBinding.btnTrailerOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    watchYoutubeVideo(getApplicationContext(), videos.get(0).getKey());
                }
            });

            activityMovieDetailsBinding.btnTrailerTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    watchYoutubeVideo(getApplicationContext(), videos.get(1).getKey());
                }
            });
        } else if (videos.size() == 1){
            activityMovieDetailsBinding.btnTrailerOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    watchYoutubeVideo(getApplicationContext(), videos.get(0).getKey());
                }
            });

            activityMovieDetailsBinding.btnTrailerTwo.setVisibility(View.INVISIBLE);
        } else {
            activityMovieDetailsBinding.btnTrailerOne.setVisibility(View.INVISIBLE);
            activityMovieDetailsBinding.btnTrailerTwo.setVisibility(View.INVISIBLE);
        }
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            Log.d(TAG, "Starting YouTube Trailer using appIntent");
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            Log.d(TAG, "Starting YouTube Trailer using webIntent");
            context.startActivity(webIntent);
        }
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