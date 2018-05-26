package com.aayushtuladhar.android.movieapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.aayushtuladhar.android.movieapp.R;
import com.aayushtuladhar.android.movieapp.model.Movie;
import com.aayushtuladhar.android.movieapp.utils.MovieDbClientUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends ArrayAdapter<Movie> {

    private static final String TAG = MoviesAdapter.class.getCanonicalName();

    private static class ViewHolder {
        final ImageView moviePoster;

        ViewHolder(View view) {
            this.moviePoster = view.findViewById(R.id.imgPoster);
        }
    }

    public MoviesAdapter(@NonNull Context context, @NonNull List<Movie> movies) {
        super(context, 0, movies);
    }

    /**
     * Performs translation between the data item and the View to display.
     * It returns the actual view used as a row within the ListView at a particular position
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_movie, parent, false);
            viewHolder = new ViewHolder(convertView);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Lookup view for data population
        String posterUrlPath = MovieDbClientUtils.POSTER_BASEURL + movie.getPosterPath();
        Log.d(TAG, "Movie Poster URL: " + posterUrlPath);

        Picasso.with(convertView.getContext())
                .load(posterUrlPath)
                .into(viewHolder.moviePoster);
        return convertView;
    }
}