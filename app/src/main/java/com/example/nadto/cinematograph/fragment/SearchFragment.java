package com.example.nadto.cinematograph.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.nadto.cinematograph.DetailedActivity;
import com.example.nadto.cinematograph.Film;
import com.example.nadto.cinematograph.FilmAdapter;
import com.example.nadto.cinematograph.Json.JsonParser;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.nadto.cinematograph.MainActivity.ARRAY;

public class SearchFragment extends ListFragment {

    private EditText mEditText;
    private FloatingActionButton fab;
    private RadioGroup radioGroup;
    private RadioButton checkedRadioButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.searchRecycler);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),  mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                if(position >= 0) {
                    intent.putExtra(DetailedActivity.TITLE, movies.get(position).getTitle());
                    startActivity(intent);
                } else {
                    Snackbar.make(rootView, "Unable to load information at (" + position + ")",Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        mEditText = (EditText)rootView.findViewById(R.id.enter_text);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radio);
        fab = (FloatingActionButton)rootView.findViewById(R.id.fabSearch);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButton = (RadioButton) rootView.findViewById(radioGroup.getCheckedRadioButtonId());
                String queryMode = "none";
                if(checkedRadioButton != null) {
                    queryMode = checkedRadioButton.getText().toString();
                }
                movies.clear();
                String query = mEditText.getText().toString();
                new GetSearchMoviesTask(ARRAY).execute(
                        createSearchURL(query,queryMode));
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
        });
        addFakeItems();

        setUpData();

        return rootView;
    }

    private URL createSearchURL(String query, String mode) {

        String apiKey = getActivity().getString(R.string.api_key);
        String baseUrl = getActivity().getString(R.string.base_url);
        String modeMovie = getActivity().getString(R.string.mode_search_movie);
        String modeTv = getActivity().getString(R.string.mode_search_tv);

        String strMode = mode.equals("Movies") ? modeMovie : modeTv;

        try {
            String urlResult = baseUrl + strMode + query + "&api_key=" + apiKey;
            return new URL(urlResult);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    void setUpData() {
        movies.clear();

        mFilmAdapter = new FilmAdapter(getActivity(), movies);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFilmAdapter);
    }

    @Override
    public void addItem(int id) {

    }

    class GetSearchMoviesTask extends AsyncTask<URL, Void, JSONObject> {

        String type;

        GetSearchMoviesTask(String type) {
            this.type = type;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected JSONObject doInBackground(URL... urls) {
            HttpURLConnection httpUrlCon = null;

            try {
                httpUrlCon = (HttpURLConnection) urls[0].openConnection();
                int response = httpUrlCon.getResponseCode();
                if(response == HttpURLConnection.HTTP_OK) {
                    StringBuilder stringBuilder = new StringBuilder();
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(httpUrlCon.getInputStream(),"UTF-8"))) {
                        String line;
                        Log.e("============","=======START READING=========");
                        Log.v("URL", urls[0].toString());
                        while((line = reader.readLine()) != null) {
                            Log.v("stream:", line);
                            stringBuilder.append(line);
                        }
                        Log.e("============","=======END READING=========");
                    } catch (IOException ex) {
                        Snackbar.make(getActivity().findViewById(R.id.activity_main),
                                getString(R.string.read_error), Snackbar.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                    return new JSONObject(stringBuilder.toString());

                } else {
                    Snackbar.make(getActivity().findViewById(R.id.activity_main),
                            getString(R.string.load_error) + "|" + response, Snackbar.LENGTH_LONG).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                Snackbar.make(getActivity().findViewById(R.id.activity_main), ex.getMessage(), Snackbar.LENGTH_LONG).show();
            } finally {
                if(httpUrlCon != null) {
                    httpUrlCon.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(jsonObject != null) {
                if(type.equals(ARRAY)) {
                    try {
                        JSONArray jsonArray = jsonObject.has("results") ? jsonObject.getJSONArray("results") : null;

                        if(jsonArray != null) {
                            for(int i = 0; i < 5; i++) {
                                JsonParser.convertJSON(jsonArray.getJSONObject(i), getActivity(), movies);
                            }
                            mFilmAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        Snackbar.make(getActivity().findViewById(R.id.activity_main), "JSON parse error", Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    JsonParser.convertJSON(jsonObject,getActivity(),movies);
                    mFilmAdapter.notifyDataSetChanged();
                }
            } else {
                Snackbar.make(getActivity().findViewById(R.id.activity_main), getString(R.string.load_error), Snackbar.LENGTH_LONG).show();
            }
        }

    }

}
