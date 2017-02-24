package com.example.nadto.cinematograph.Json;


import android.content.Context;

import com.example.nadto.cinematograph.Film;
import com.example.nadto.cinematograph.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class JsonParser {

    public static void convertJSON(JSONObject jsonObject, Context context, ArrayList<Film> arrayList) {

        try {

            if(jsonObject.has("status_code")) {

                return;
            }

            JSONArray productionCompanies = jsonObject.has("production_companies") ? jsonObject.getJSONArray("production_companies") : null;
            JSONArray createdBy = jsonObject.has("created_by") ? jsonObject.getJSONArray("created_by") : null;

            String author = "none";

            if(createdBy != null) {
                if(createdBy.length() > 0) {
                    author = createdBy.getJSONObject(0).getString("name");
                }
            } else if(productionCompanies != null) {
                if(productionCompanies.length() > 0) {
                    author = productionCompanies.getJSONObject(0).getString("name");
                }
            }

            String genresString = "none";

            if(jsonObject.has("genres")) {

                JSONArray genres = jsonObject.getJSONArray("genres");

                genresString = "";

                for(int i = 0; i < genres.length(); i++) {
                    if(i + 1 == genres.length()) {
                        genresString += genres.getJSONObject(i).getString("name") + ".";
                    } else {
                        genresString += genres.getJSONObject(i).getString("name") + ", ";
                    }
                }

            }

            JSONArray countries = jsonObject.has("origin_country") ? jsonObject.getJSONArray("origin_country") : null;
            String countriesString = "none";
            if(countries != null) {
                if(countries.length() > 0) {
                    countriesString = countries.toString(1);
                }
            }

            String pathToBackdrop = "none";
            String pathToPoster = "none";

            if(jsonObject.has("backdrop_path")) {
                pathToBackdrop = context.getString(R.string.image_base) + jsonObject.getString("backdrop_path");
            }

            if(jsonObject.has("poster_path")) {
                pathToPoster = context.getString(R.string.image_base) + jsonObject.getString("poster_path");
            }

            String titleStr = jsonObject.has("original_title") ? jsonObject.getString("original_title") : null;
            String nameStr = jsonObject.has("original_name") ? jsonObject.getString("original_name") : null;

            String title = titleStr != null ? titleStr : nameStr != null ? nameStr : "none";

            String firstAirDate = jsonObject.has("first_air_date") ? jsonObject.getString("first_air_date") : null;
            String releaseDate = jsonObject.has("release_date") ? jsonObject.getString("release_date") : null;
            String airDate = jsonObject.has("air_date") ? jsonObject.getString("air_date") : null;

            String year = "none";

            if(firstAirDate != null) {
                year = firstAirDate;
            } else if(releaseDate != null) {
                year = releaseDate;
            } else if(airDate != null) {
                year = airDate;
            }

            int type = jsonObject.has("episode_run_time") ? Film.TV : Film.MOVIE;

            int id = jsonObject.has("id") ? 0 : jsonObject.getInt("id");
            float voteAverage = jsonObject.has("vote_average") ? (float) jsonObject.getDouble("vote_average") : .0f;

            arrayList.add(new Film(
                    id,
                    type,
                    title,
                    year,
                    author,
                    voteAverage,
                    genresString,
                    pathToPoster,
                    pathToBackdrop,
                    countriesString)
            );

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    public static URL createURL(int id, int type, Context context) {

        String apiKey = context.getString(R.string.api_key);
        String baseUrl = context.getString(R.string.base_url_movie);
        String baseUrlTv = context.getString(R.string.base_url_tv);

        try {

            String res;

            if(type == Film.MOVIE) {
                res = baseUrl;
            } else {
                res = baseUrlTv;
            }

            String urlResult = res + id + "?api_key=" + apiKey;

            return new URL(urlResult);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
