package com.example.nadto.cinematograph.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nadto.cinematograph.HttpHelper.LoadImageTask;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.model.Film;

import java.util.ArrayList;


public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    private ArrayList<Film> films;
    private Context mContext;


    public FilmAdapter(Context context, ArrayList<Film> films) {
        this.films = films;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(films.get(position));
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView title, year;
        private ImageView backdrop;
        private RatingBar voteAverage;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.cardView);
            title = (TextView)itemView.findViewById(R.id.title);
            year = (TextView)itemView.findViewById(R.id.year);
            voteAverage = (RatingBar)itemView.findViewById(R.id.voteAverage);
            backdrop = (ImageView)itemView.findViewById(R.id.backdrop);

        }

        void bind(final Film film) {

            title.setText(film.getTitle());
            String date = film.getDate().length() >= 4 ? film.getDate().substring(0,4) : "none";
            year.setText(mContext.getString(R.string.year_template, date));
            voteAverage.setRating((film.getVoteAverage() * 5.0f) / 10.0f);

            LoadImageTask loadImageTask = new LoadImageTask(backdrop);
            loadImageTask.execute(film.getPathToBackdrop().equals("none") ? film.getPathToPoster() : film.getPathToBackdrop());

        }
    }

}
