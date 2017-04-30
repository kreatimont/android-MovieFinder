package io.kreatimont.cinematograph.ui.tv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;

import io.kreatimont.cinematograph.ui.proto.adapter.CardLayoutType;
import io.kreatimont.cinematograph.ui.proto.adapter.ProtoAdapter;
import io.kreatimont.cinematograph.ui.proto.adapter.ResponseRecyclerViewAdapter;
import io.kreatimont.cinematograph.api.model.tmdb.tv.Tv;
import io.kreatimont.cinematograph.ui.proto.viewHolder.BaseViewHolder;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TvAdapter extends ProtoAdapter {

    public TvAdapter(Context context, ArrayList data) {
        super(context, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind((Tv) mDataList.get(position));
    }

}
