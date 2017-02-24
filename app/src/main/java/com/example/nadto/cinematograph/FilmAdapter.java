package com.example.nadto.cinematograph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        private TextView title, year, genres, createdBy, voteAverage;
        private ImageView backdrop;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.cardView);
            title = (TextView)itemView.findViewById(R.id.title);
            year = (TextView)itemView.findViewById(R.id.year);
            createdBy = (TextView)itemView.findViewById(R.id.createdBy);
            voteAverage = (TextView)itemView.findViewById(R.id.voteAverage);
            backdrop = (ImageView)itemView.findViewById(R.id.backdrop);
            genres = (TextView)itemView.findViewById(R.id.genres);

        }

        void bind(final Film film) {

            title.setText(film.getTitle());
            String date = film.getDate().length() >= 4 ? film.getDate().substring(0,4) : "none";
            year.setText(mContext.getString(R.string.year_template, date));
            createdBy.setText(film.getCreatedBy());
            voteAverage.setText(String.valueOf(film.getVoteAverage()));
            genres.setText(film.getGenres());
            LoadImageTask loadImageTask = new LoadImageTask(backdrop);
            loadImageTask.execute(film.getPathToBackdrop().equals("none") ? film.getPathToPoster() : film.getPathToBackdrop());

        }
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView image;

        public LoadImageTask(ImageView image) {
            this.image = image;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try {

                URL url = new URL(strings[0]);
                connection = (HttpURLConnection)url.openConnection();

                try(InputStream inputStream = connection.getInputStream()) {
                    bitmap = BitmapFactory.decodeStream(inputStream);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
            }
            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }

}
