package com.example.nadto.cinematograph.HttpHelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nadto.cinematograph.fragment.Client;
import com.example.nadto.cinematograph.model.Film;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.model.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JsonHelper {

    public static final String JSON_ARRAY = "array";

    private Context context;
    private Client client;

    public JsonHelper(Context context) {
        this.context = context;
        this.client = (Client) context;
    }

    public JsonHelper(Context context, Client client) {
        this.context = context;
        this.client = client;
    }

    public ArrayList<Film> convertJsonToFilmList(JSONObject jsonObject) {

        ArrayList<Film> films = new ArrayList<>();

        try {

            if(jsonObject.has("status_code")) {
                return null;
            }

            JSONArray jsonArray = jsonObject.has("results") ? jsonObject.getJSONArray("results") : null;

            if(jsonArray == null) {
                return null;
            }

            for(int i = 0; i < jsonArray.length(); i++) {
                films.add(convertJsonToFilm(jsonArray.getJSONObject(i)));
            }

        } catch (JSONException ex) {
            Log.e("TAG","Parse json problem");
            Log.e("TAG",ex.toString());
        }

        return films;
    }

    public Person convertJsonToPerson(JSONObject jsonObject) {

        if(jsonObject == null) {
            return null;
        }

        try {

            int id = jsonObject.getInt("id");

            String name  = jsonObject.has("name") ? jsonObject.getString("name") : "none";
            String charName = jsonObject.has("character") ? jsonObject.getString("character") : "none";

            String profilePath = context.getString(R.string.image_base);
            profilePath += jsonObject.has("profile_path") ? jsonObject.getString("profile_path") : "none";

            String biography = jsonObject.has("biography") ? jsonObject.getString("biography") : "none";
            String birthday = jsonObject.has("birthday") ? jsonObject.getString("birthday") : "none";
            String deathday = jsonObject.has("deathday") ? jsonObject.getString("deathday") : "none";
            String placeOfBirth = jsonObject.has("place_of_birth") ? jsonObject.getString("place_of_birth") : "none";

            Double popularity = jsonObject.has("popularity") ? jsonObject.getDouble("popularity") : 0.0;

            int gender = jsonObject.has("gender") ? jsonObject.getInt("gender") : 0;
            String genderString = gender == 2 ? "Male" : "Female";

            String link = jsonObject.has("homepage") ? jsonObject.getString("homepage") : "none";

            return new Person(id,name,charName,profilePath,biography,birthday,deathday,placeOfBirth,genderString,link,popularity);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Person> getCreditsFromJsonObject(JSONObject jsonObject) {

        ArrayList<Person> castArray = new ArrayList<>();

        if(jsonObject != null) {

            if(jsonObject.has("credits")) {

                try {

                    JSONObject credits = jsonObject.getJSONObject("credits");

                    JSONArray cast = credits.has("cast") ? credits.getJSONArray("cast") : null;

                    if(cast != null) {

                        for(int i = 0; i < cast.length(); i++) {

                            JSONObject character = cast.getJSONObject(i);

                            int id = character.getInt("id");
                            String name  = character.has("name") ? character.getString("name") : "none";
                            String charName = character.has("character") ? character.getString("character") : "none";

                            String profilePath = context.getString(R.string.image_base);

                           profilePath += character.has("profile_path") ? character.getString("profile_path") : "none";

                            castArray.add(new Person(id,name,charName,profilePath));

                        }

                    }

                } catch (JSONException ex) {
                    Log.e("TAG",ex.toString());
                }

            }

        }

        return castArray;

    }

    public Film convertJsonToFilm(JSONObject jsonObject) {

        try {

            if(jsonObject == null) {
                return null;
            }

            if(jsonObject.has("status_code")) {
                return null;
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

            String status = Film.STATUS_RELEASED;

            if(jsonObject.has("status")) {
                status = jsonObject.getString("status");
            }

            String overview = jsonObject.has("overview") ? jsonObject.getString("overview") : "none";
            String tagline = jsonObject.has("tagline") ? jsonObject.getString("tagline") : "none";

            int budget = jsonObject.has("budget") ? jsonObject.getInt("budget") : 0;
            float popularity = jsonObject.has("popularity") ? (float)jsonObject.getDouble("popularity") : .0f;

            int type = Film.MOVIE;

            if(jsonObject.has("first_air_date") || jsonObject.has("episode_run_time")) {
                type = Film.TV;
            }

            String firstAirDate = jsonObject.has("first_air_date") ? jsonObject.getString("first_air_date") : null;
            String releaseDate = jsonObject.has("release_date") ? jsonObject.getString("release_date") : null;

            String year = "none";

            if(firstAirDate != null) {
                year = firstAirDate;
            } else if(releaseDate != null) {
                year = releaseDate;
            }

            int id = jsonObject.has("id") ? jsonObject.getInt("id") : 0;
            float voteAverage = jsonObject.has("vote_average") ? (float) jsonObject.getDouble("vote_average") : .0f;

            return new Film(
                    id,
                    type,
                    status,
                    title,
                    year,
                    author,
                    voteAverage,
                    genresString,
                    pathToPoster,
                    pathToBackdrop,
                    countriesString,
                    overview,
                    tagline,
                    popularity,
                    budget
            );

        } catch (JSONException ex) {
            Log.e("TAG","Parse json problem");
            Log.e("TAG",ex.toString());
        }

        return null;


    }

    public URL createURL(int id, int type) {

        String apiKey = context.getString(R.string.api_key);
        String apiKeyPrefix = context.getString(R.string.api_key_prefix);
        String baseUrl = context.getString(R.string.base_url_movie);
        String baseUrlTv = context.getString(R.string.base_url_tv);
        String baseUrlPerson= context.getString(R.string.base_url_person);
        String creditsMode = context.getString(R.string.credits_mode);


        try {

            String res;

            if(type == Film.MOVIE) {
                res = baseUrl;
            } else if(type == Film.TV) {
                res = baseUrlTv;
            } else {
                Log.e("Film.PERSON:", baseUrlPerson + id + apiKeyPrefix + apiKey);
                return new URL(baseUrlPerson + id + apiKeyPrefix + apiKey);
            }

            String urlResult = res + id + apiKeyPrefix + apiKey + creditsMode;

            Log.e("TAG",urlResult);

            return new URL(urlResult);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void loadJson(URL url) {
        new GetJsonTask().execute(url);
    }

    private class GetJsonTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            HttpURLConnection httpUrlCon = null;

            try {
                Log.e("GetJsonTask url",urls[0].toString());
                httpUrlCon = (HttpURLConnection) urls[0].openConnection();
                int response = httpUrlCon.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder stringBuilder = new StringBuilder();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpUrlCon.getInputStream(), "UTF-8"));
                        String line;
                        Log.v("URL", urls[0].toString());
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        reader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return new JSONObject(stringBuilder.toString());

                } else {
                    Log.v("TAG", response + "");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.v("TAG", ex.toString());
            } finally {
                if (httpUrlCon != null) {
                    httpUrlCon.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            client.setData(jsonObject);
        }

    }

}
