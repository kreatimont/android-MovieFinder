package io.kreatimont.cinematograph.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import io.kreatimont.cinematograph.model.tmdb.tv.Tv;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.ViewHolder> implements ResponseRecyclerViewAdapter {

    private ArrayList<Tv> tvArrayList;
    private Context mContext;
    private CardLayoutType layoutType = CardLayoutType.LinearWithBackdrop;


    public TvAdapter(Context context, ArrayList<Tv> tvArrayList) {
        this.tvArrayList = tvArrayList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        return new TvAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(tvArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return tvArrayList.size();
    }

    @Override
    public void setLayout(CardLayoutType type) {
        this.layoutType = type;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, year;
        private ImageView backdrop;
        private RatingBar voteAverage;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.title);
            year = (TextView)itemView.findViewById(R.id.year);
            voteAverage = (RatingBar)itemView.findViewById(R.id.voteAverage);
            backdrop = (ImageView)itemView.findViewById(R.id.backdrop);

        }

        void bind(final Tv tv) {

            title.setText(tv.getName());

            String yearString = tv.getFirstAirDate() != null && tv.getFirstAirDate().length() > 4 ? tv.getFirstAirDate().substring(0,4) : "N/A";
            year.setText(mContext.getString(R.string.year_template, yearString));
            voteAverage.setRating((float) ((tv.getVoteAverage() * 5.0f) / 10.0f));

            String path;

            switch (layoutType) {
                case Grid:
                    path = tv.getPosterPath();
                    break;
                case LinearWithBackdrop:
                    path = tv.getBackdropPath();
                    break;
                case LinearWithPoster:
                    path = tv.getPosterPath();
                    break;
                default:
                    path = tv.getBackdropPath();
            }

            Picasso.with(mContext).load(mContext.getString(R.string.image_base) + mContext.getString(R.string.backdrop_size_medium) + path).into(backdrop);
        }
    }
}