package io.kreatimont.cinematograph.ui.movie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nadto.cinematograph.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

import java.util.List;

import io.kreatimont.cinematograph.data.api.MovieApi;
import io.kreatimont.cinematograph.data.model.response.MoviesResponse;
import io.kreatimont.cinematograph.data.model.tmdb.movie.Movie;
import io.kreatimont.cinematograph.data.service.MovieService;
import io.kreatimont.cinematograph.helpers.EndlessRecyclerViewScrollListener;
import io.kreatimont.cinematograph.utils.Cinematograph;
import retrofit2.Call;
import retrofit2.Response;

public class TestMovieFragment extends Fragment {

    private MovieService mService;
    private List<Movie> mMovies;
    private RecyclerView mMovieRecycler;
    private FastItemAdapter<Movie> mMovieAdapter;
    private FooterAdapter<ProgressItem> mFooterAdapter;
    private int mPage;
    private String mType;

    public TestMovieFragment() {
    }

    public static Fragment newInstance(String type) {
        TestMovieFragment testMovieFragment = new TestMovieFragment();

        Bundle args = new Bundle();
        args.putString("type", type);
        testMovieFragment.setArguments(args);
        testMovieFragment.provideTestSharedData();
        return testMovieFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString("type", "All");
        mService = MovieService.getService(getString(R.string.api_key), "ru");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        mPage = 1;

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mMovieRecycler = (RecyclerView) rootView.findViewById(R.id.movieRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mMovieRecycler.setLayoutManager(linearLayoutManager);
        mMovieRecycler.setItemViewCacheSize(20);

        mMovieAdapter = new FastItemAdapter<>();
        mMovieAdapter.withOnClickListener(onClickListener);

        mFooterAdapter = new FooterAdapter<>();

        mMovieRecycler.setAdapter(mFooterAdapter.wrap(mMovieAdapter));
        mMovieRecycler.addOnScrollListener(new EndlessRecyclerOnScrollListener(mFooterAdapter) {
            @Override
            public void onLoadMore(int currentPage) {
                mFooterAdapter.clear();
                mFooterAdapter.add(new ProgressItem().withEnabled(true));
                loadMore();
            }
        });

        fetchNew();
        return rootView;
    }


    private FastAdapter.OnClickListener<Movie> onClickListener = new FastAdapter.OnClickListener<Movie>() {
        @Override
        public boolean onClick(View v, IAdapter<Movie> adapter, Movie item, int position) {
            return false;
        }
    };

    private void loadMore() {

        MovieService.OnRequestAllMoviesListener requestAllMoviesListener
                = new MovieService.OnRequestAllMoviesListener() {
            @Override
            public void onRequestAllMoviesSuccess(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if(response.code() == 200) {
                    mMovies = response.body().getResults();
                    mFooterAdapter.clear();
                    mMovieAdapter.add(mMovies);
                    mPage++;
                }
            }

            @Override
            public void onRequestAllMoviesFailed(Call<MoviesResponse> call, Throwable throwable) {

            }
        };

        mService.requestPopularMovies(mPage, requestAllMoviesListener);

    }

    private void fetchNew() {
        mPage = 1;

        MovieService.OnRequestAllMoviesListener onRequestAllMoviesListener =
                new MovieService.OnRequestAllMoviesListener() {
                    @Override
                    public void onRequestAllMoviesSuccess(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        if(response.code() == 200) {
                            mMovies = response.body().getResults();
                            mFooterAdapter.clear();
                            mMovieAdapter.add(mMovies);
                            mPage++;
                        }
                    }

                    @Override
                    public void onRequestAllMoviesFailed(Call<MoviesResponse> call, Throwable throwable) {

                    }
                };

        mService.requestPopularMovies(mPage, onRequestAllMoviesListener);
    }

    private void provideTestSharedData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Cinematograph.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("movie_layout", "h_list_poster");
        editor.apply();
    }

}
