package io.kreatimont.cinematograph.ui.proto.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import com.squareup.picasso.Picasso;

import io.kreatimont.cinematograph.api.model.tmdb.Genre;
import io.kreatimont.cinematograph.api.model.tmdb.movie.Movie;
import io.kreatimont.cinematograph.api.model.tmdb.tv.Tv;
import io.kreatimont.cinematograph.ui.proto.adapter.MovieCardLayoutType;

import static com.example.nadto.cinematograph.R.id.voteAverage;
import static com.example.nadto.cinematograph.R.id.year;

public class MovieMaterialViewHolder extends RecyclerView.ViewHolder {

    private TextView title, rating, genres, overview;
    private ImageView backdrop;
    private MovieCardLayoutType layoutType = MovieCardLayoutType.LinearWithPoster;
    private Context mContext;

    public MovieMaterialViewHolder(View itemView, Context context, MovieCardLayoutType layoutType) {
        super(itemView);

        this.mContext = context;
        this.layoutType = layoutType;

        title = (TextView)itemView.findViewById(R.id.title);
        genres = (TextView)itemView.findViewById(R.id.genres);
        rating = (TextView)itemView.findViewById(R.id.rating);
        overview = (TextView)itemView.findViewById(R.id.overview);

        backdrop = (ImageView)itemView.findViewById(R.id.backdrop);
    }

    public void bind(final Movie movie) {

        title.setText(movie.getTitle());
        if(movie.getGenres() != null) {
            for(Genre g : movie.getGenres()) {
                if((movie.getGenres().indexOf(g) + 1) == movie.getGenres().size()) {
                    genres.append(g.getName());
                }
                genres.append(g.getName() + ", ");
            }
        }

        rating.setText(mContext.getString(R.string.rating_template, movie.getVoteAverage()));
        overview.setText(movie.getOverview());

        Picasso.with(mContext).load(mContext.getString(R.string.image_base) + mContext.getString(R.string.backdrop_size_medium) + movie.getPosterPath()).into(backdrop);
    }

    public void bind(final Tv movie) {

        title.setText(movie.getName());
        if(movie.getGenres() != null) {
            for(Genre g : movie.getGenres()) {
                if((movie.getGenres().indexOf(g) + 1) == movie.getGenres().size()) {
                    genres.append(g.getName());
                }
                genres.append(g.getName() + ", ");
            }
        }
        rating.setText(mContext.getString(R.string.rating_template, movie.getVoteAverage()));
        overview.setText(movie.getOverview());

        Picasso.with(mContext).load(mContext.getString(R.string.image_base) + mContext.getString(R.string.backdrop_size_medium) + movie.getPosterPath()).into(backdrop);
    }

}
