package io.kreatimont.cinematograph.ui.proto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nadto.cinematograph.R;

import java.util.ArrayList;
import java.util.List;

import io.kreatimont.cinematograph.api.model.tmdb.movie.Movie;
import io.kreatimont.cinematograph.api.model.tmdb.tv.Tv;
import io.kreatimont.cinematograph.ui.proto.viewHolder.BaseViewHolder;
import io.realm.RealmObject;

public abstract class ProtoAdapter extends RecyclerView.Adapter<BaseViewHolder> implements ResponseRecyclerViewAdapter {

    public List mDataList;
    public Context mContext;
    public CardLayoutType layoutType = CardLayoutType.LinearWithPoster;

    public ProtoAdapter(Context context, ArrayList data) {
        this.mContext = context;
        this.mDataList = data;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        int layoutId;
        switch (layoutType) {
            case Grid:
                layoutId = R.layout.card_movie_grid;
                break;
            case LinearWithBackdrop:
                layoutId = R.layout.card_movie;
                break;
            case LinearWithPoster:
                layoutId = R.layout.card_movie_v2;
                break;
            default:
                layoutId = R.layout.card_movie;
        }
        view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);

        return new BaseViewHolder(view, mContext, layoutType);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void setLayout(CardLayoutType type) {
        this.layoutType = type;
    }
}
