package com.aayushtuladhar.android.movieapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aayushtuladhar.android.movieapp.data.FavoriteMovie;
import com.aayushtuladhar.android.movieapp.databinding.ActivityMovieDetailsBinding;
import com.aayushtuladhar.android.movieapp.model.Movie;
import com.aayushtuladhar.android.movieapp.model.MovieReviewsResponse;
import com.aayushtuladhar.android.movieapp.model.MovieVideo;
import com.aayushtuladhar.android.movieapp.model.MovieVideosResponse;
import com.aayushtuladhar.android.movieapp.model.Review;
import com.aayushtuladhar.android.movieapp.utils.AppDatabase;
import com.aayushtuladhar.android.movieapp.utils.MovieDbClientUtils;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aayushtuladhar.android.movieapp.FavoriteMoviesActivity.MOVIE_ID;

public class MovieDetailsActivity extends AppCompatActivity{

    public static final String MOVIE = "MOVIE";
    private static final String TAG = MovieDetailsActivity.class.getCanonicalName();
    private ActivityMovieDetailsBinding activityMovieDetailsBinding;
    private Movie mMovie;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        activityMovieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        if (intent.hasExtra(MOVIE)){
            mMovie = (Movie) intent.getSerializableExtra(MOVIE);

            populateView(mMovie);
            getDetailsForMovie(mMovie.getId());


        } else if (intent.hasExtra(MOVIE_ID)){
            Integer movieId = intent.getIntExtra(MOVIE_ID, 0);

            Call<Movie> movieForId = MovieDbClientUtils.getMovieForId(movieId);
            movieForId.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if (response.isSuccessful()){
                        mMovie = response.body();
                        populateView(mMovie);
                        getDetailsForMovie(mMovie.getId());
                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {

                }
            });
        }

    }

    private void getDetailsForMovie(Integer movieId){

        // Make additional Calls for Movie Reviews and Trailer
        getTrailerForMovie(movieId);
        getReviewsForMovie(movieId);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void getTrailerForMovie(final Integer movieId){
        Log.i(TAG, "Getting Trailer for Movie: " + movieId);

        Call<MovieVideosResponse> videosForMovie = MovieDbClientUtils.getVideosForMovie(movieId);
        videosForMovie.enqueue(new Callback<MovieVideosResponse>() {
            @Override
            public void onResponse(Call<MovieVideosResponse> call, Response<MovieVideosResponse> response) {
                if (response.isSuccessful()){
                    final List<MovieVideo> videos = response.body().getMovieVideos();
                    Log.i(TAG, "Got Trailers for Movie: " + videos.size());
                    addTrailersForMovie(videos);
                } else{
                    Log.e(TAG, response.code() + " - Response Code; Error getting Videos for Movie: (Failed Response)" + movieId);
                }
            }

            @Override
            public void onFailure(Call<MovieVideosResponse> call, Throwable t) {
                Log.e(TAG, "Error getting Trailers for Movie: " + movieId);
                closeOnError();

            }
        });
    }

    private void getReviewsForMovie(final Integer movieId){
        Log.i(TAG, "Getting Reviews for Movie: " + movieId);

        Call<MovieReviewsResponse> reviewsResponseCall = MovieDbClientUtils.getReviewsForMovie(movieId);
        reviewsResponseCall.enqueue(new Callback<MovieReviewsResponse>() {
            @Override
            public void onResponse(Call<MovieReviewsResponse> call, Response<MovieReviewsResponse> response) {
                if (response.isSuccessful()){
                    final List<Review> reviews = response.body().getReviews();
                    Log.i(TAG, "Got Review for Movie: " + reviews.size());

                    addReviewsForMovie(reviews);
                } else {
                    Log.e(TAG, response.code() + " - Response Code; Error getting Reviews for Movie: (Failed Response)" + movieId);
                }
            }

            @Override
            public void onFailure(Call<MovieReviewsResponse> call, Throwable t) {
                Log.e(TAG, "Error getting Reviews for Movie: " + movieId);
                closeOnError();
            }
        });
    }

    private void addReviewsForMovie(final List<Review> reviews){
        if (reviews.size() >= 1){

            //TODO: Convert to ArrayAdapter
            StringBuilder sb = new StringBuilder();
            for (Review review : reviews) {
                sb.append("Author: " + review.getAuthor() + "\n");
                sb.append("Review: "  + review.getContent() + "\n");
                sb.append("\n ____________________________________ \n");
            }
            activityMovieDetailsBinding.textViewReviews.setText(sb.toString());
        } else {
            activityMovieDetailsBinding.textViewReviewText.setVisibility(View.INVISIBLE);
            activityMovieDetailsBinding.textViewReviews.setVisibility(View.INVISIBLE);
        }
    }



    private void addTrailersForMovie(final List<MovieVideo> videos){
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

    public void addToFavorite(View view){
        Integer movieId = mMovie.getId();
        Log.i(TAG, "Adding Movie to Favorite: " + movieId.toString());
        mDb = AppDatabase.getAppDatabase(this);

        FavoriteMovie favoriteMovie = new FavoriteMovie(mMovie.getId(), mMovie.getTitle(), mMovie.getPosterPath(), mMovie.getReleaseDate());

        //Adding Favorite Movie to Database
        new AddToFavoriteAsync().execute(favoriteMovie);
    }

    class AddToFavoriteAsync extends AsyncTask<FavoriteMovie, Void, Boolean>{

        @Override
        protected Boolean doInBackground(FavoriteMovie... favoriteMovies) {
            mDb.favoriteMovieDao().insertAll(favoriteMovies);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.i(TAG, "Movie Added to Database");
        }
    }
}