package io.kreatimont.cinematograph.ui.movie;

import android.content.Context;

import java.util.ArrayList;

import io.kreatimont.cinematograph.api.model.tmdb.movie.Movie;
import io.kreatimont.cinematograph.ui.proto.adapter.ProtoMovieAdapter;
import io.kreatimont.cinematograph.ui.proto.viewHolder.MovieViewHolder;

public class MovieMovieAdapter extends ProtoMovieAdapter {

    public MovieMovieAdapter(Context context, ArrayList data) {
        super(context, data);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind((Movie)mDataList.get(position));
    }

}
