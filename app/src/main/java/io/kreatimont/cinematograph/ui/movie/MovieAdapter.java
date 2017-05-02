package io.kreatimont.cinematograph.ui.movie;

import android.content.Context;

import java.util.ArrayList;

import io.kreatimont.cinematograph.data.model.tmdb.movie.Movie;
import io.kreatimont.cinematograph.ui.proto.adapter.ProtoMovieAdapter;
import io.kreatimont.cinematograph.ui.proto.viewHolder.MovieViewHolder;

public class MovieAdapter extends ProtoMovieAdapter {

    public MovieAdapter(Context context, ArrayList data) {
        super(context, data);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind((Movie)mDataList.get(position));
    }

}
