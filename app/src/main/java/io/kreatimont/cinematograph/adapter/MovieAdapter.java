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

import io.kreatimont.cinematograph.model.tmdb.movie.Movie;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements ResponseRecyclerViewAdapter {

    private ArrayList<Movie> movies;
    private Context mContext;
    private CardLayoutType layoutType = CardLayoutType.LinearWithPoster;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.movies = movies;
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

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
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

        void bind(final Movie movie) {

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
                default:
                    path = movie.getBackdropPath();
            }

            Picasso.with(mContext).load(mContext.getString(R.string.image_base) + mContext.getString(R.string.backdrop_size_medium) + path).into(backdrop);
        }
    }

}
