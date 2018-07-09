package com.aayushtuladhar.android.movieapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aayushtuladhar.android.movieapp.R;
import com.aayushtuladhar.android.movieapp.data.FavoriteMovie;

import java.util.List;

public class FavoriteMovieAdapter extends ArrayAdapter<FavoriteMovie>{

    public FavoriteMovieAdapter(@NonNull Context context, @NonNull List<FavoriteMovie> favoriteMovies) {
        super(context, 0, favoriteMovies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_favorite_movie, parent, false);
        }

        FavoriteMovie favoriteMovie = getItem(position);
        TextView movieId = convertView.findViewById(R.id.txtMovieName);
        movieId.setText(favoriteMovie.getTitle());

        return convertView;
    }
}
