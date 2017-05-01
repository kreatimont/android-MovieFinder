package io.kreatimont.cinematograph.ui.tv;

import android.content.Context;

import io.kreatimont.cinematograph.ui.proto.adapter.ProtoMovieAdapter;
import io.kreatimont.cinematograph.api.model.tmdb.tv.Tv;
import io.kreatimont.cinematograph.ui.proto.viewHolder.MovieMaterialViewHolder;
import io.kreatimont.cinematograph.ui.proto.viewHolder.MovieViewHolder;

import java.util.ArrayList;

public class TvAdapter extends ProtoMovieAdapter {

    public TvAdapter(Context context, ArrayList data) {
        super(context, data);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind((Tv) mDataList.get(position));
    }

}
