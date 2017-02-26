package com.example.nadto.cinematograph.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.example.nadto.cinematograph.HttpHelper.JsonHelper;
import com.example.nadto.cinematograph.activity.DetailedActivity;
import com.example.nadto.cinematograph.adapter.FilmAdapter;
import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.adapter.RecyclerItemClickListener;

import org.json.JSONObject;

import java.net.URL;

public class SearchFragment extends ListFragment {

    private EditText mEditText;
    private RadioGroup radioGroup;
    private RadioButton checkedRadioButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.searchRecycler);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                if(position >= 0) {
                    intent.putExtra(DetailedActivity.EXTRA_ID, movies.get(position).getId());
                    intent.putExtra(DetailedActivity.EXTRA_TYPE, movies.get(position).getType());
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

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabSearch);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkedRadioButton = (RadioButton) rootView.findViewById(radioGroup.getCheckedRadioButtonId());

                String queryMode = checkedRadioButton != null ? checkedRadioButton.getText().toString() : "none";

                String query = mEditText.getText().toString();

                jsonHelper.loadJson(createSearchURL(query, queryMode));

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
        });

        addFakeItems();

        jsonHelper = new JsonHelper(getActivity(), this);

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
            Log.e("SearchFragment", urlResult);
            return new URL(urlResult);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    void setUpData() {
        movies.clear();

        if(mFilmAdapter == null) {
            mFilmAdapter = new FilmAdapter(getActivity(), movies);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(mFilmAdapter);
        }

    }

    @Override
    public void setData(JSONObject jsonObject) {
        movies.clear();
        movies = jsonHelper.convertJsonToFilmList(jsonObject);

        mFilmAdapter = null;
        mFilmAdapter = new FilmAdapter(getActivity(), movies);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFilmAdapter);

        mFilmAdapter.notifyDataSetChanged();
    }
}
