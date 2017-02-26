package com.example.nadto.cinematograph.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nadto.cinematograph.R;

public class MovieFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String TOP_MOVIES =
                "https://api.themoviedb.org/3/discover/movie?api_key="
                        + getActivity().getString(R.string.api_key) + "&sort_by=popularity.desc&include_adult=true";
        this.setFilmType(TOP_MOVIES);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
