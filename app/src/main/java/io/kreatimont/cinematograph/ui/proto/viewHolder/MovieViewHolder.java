package io.kreatimont.cinematograph.ui.proto.viewHolder;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import com.squareup.picasso.Picasso;

import io.kreatimont.cinematograph.data.model.tmdb.movie.Movie;
import io.kreatimont.cinematograph.data.model.tmdb.tv.Tv;
import io.kreatimont.cinematograph.ui.proto.adapter.MovieCardLayoutType;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private TextView title, year;
    private ImageView backdrop;
    private RatingBar voteAverage;
    private MovieCardLayoutType layoutType = MovieCardLayoutType.LinearWithPoster;
    private Context mContext;

    public MovieViewHolder(View itemView, Context context, MovieCardLayoutType layoutType) {
        super(itemView);

        this.mContext = context;
        this.layoutType = layoutType;

        title = (TextView)itemView.findViewById(R.id.title);
        year = (TextView)itemView.findViewById(R.id.year);
        voteAverage = (RatingBar)itemView.findViewById(R.id.voteAverage);
        backdrop = (ImageView)itemView.findViewById(R.id.backdrop);

    }

    public void bind(final Movie movie) {

        title.setText(movie.getTitle());
        String yearString = movie.getReleaseDate() != null && movie.getReleaseDate().length() > 4 ? movie.getReleaseDate().substring(0,4) : "N/A";
        year.setText(mContext.getString(R.string.year_template, yearString));
        voteAverage.setRating((float) ((movie.getVoteAverage() * 5.0f) / 10.0f));

        String path;

        switch (layoutType) {
            case Grid:
                path = movie.getPosterPath();
                break;
            case LinearWithBackdrop:
                path = movie.getBackdropPath();
                break;
            case LinearWithPoster:
                path = movie.getPosterPath();
                break;
            case LinearHorizontal:
                path = movie.getPosterPath();
                break;
            default:
                path = movie.getBackdropPath();
        }

        Picasso.with(mContext).load(mContext.getString(R.string.image_base) + mContext.getString(R.string.backdrop_size_medium) + path).into(backdrop);
    }

    public void bind(final Tv movie) {

        title.setText(movie.getName());
        String yearString = movie.getFirstAirDate() != null ? movie.getFirstAirDate().substring(0,4) : "N/A";
        year.setText(mContext.getString(R.string.year_template, yearString));
        voteAverage.setRating((float) ((movie.getVoteAverage() * 5.0f) / 10.0f));

        String path;

        switch (layoutType) {
            case Grid:
                path = movie.getPosterPath();
                break;
            case LinearWithBackdrop:
                path = movie.getBackdropPath();
                break;
            case LinearWithPoster:
                path = movie.getPosterPath();
                break;
            default:
                path = movie.getBackdropPath();
        }

        Picasso.with(mContext).load(mContext.getString(R.string.image_base) + mContext.getString(R.string.backdrop_size_medium) + path).into(backdrop);
    }
}
