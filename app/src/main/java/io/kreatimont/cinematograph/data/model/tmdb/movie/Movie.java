package io.kreatimont.cinematograph.data.model.tmdb.movie;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import io.kreatimont.cinematograph.data.model.tmdb.Genre;
import io.kreatimont.cinematograph.data.model.tmdb.ProductionCompany;
import io.kreatimont.cinematograph.data.model.tmdb.ProductionCountry;
import io.kreatimont.cinematograph.data.model.tmdb.credits.Credits;
import io.kreatimont.cinematograph.utils.Cinematograph;
import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;

public class Movie extends AbstractItem<Movie, Movie.ViewHolder> {

    @SerializedName("adult")
    @Expose
    private Boolean adult;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("belongs_to_collection")
    @Expose
    private Collection belongsToCollection;

    @SerializedName("budget")
    @Expose
    private Integer budget;

    @SerializedName("genres")
    @Expose
    private RealmList<Genre> genres = null;

    @SerializedName("homepage")
    @Expose
    private String homepage;

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("imdb_id")
    @Expose
    private String imdbId;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("popularity")
    @Expose
    private Double popularity;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("production_companies")
    @Expose
    private RealmList<ProductionCompany> productionCompanies = null;

    @SerializedName("production_countries")
    @Expose
    private RealmList<ProductionCountry> productionCountries = null;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("revenue")
    @Expose
    private Integer revenue;

    @SerializedName("runtime")
    @Expose
    private Integer runtime;

    @SerializedName("spoken_languages")
    @Expose
    private RealmList<SpokenLanguage> spokenLanguages = null;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("tagline")
    @Expose
    private String tagline;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("video")
    @Expose
    private Boolean video;

    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    @SerializedName("credits")
    @Expose
    private Credits credits;


    /**
     * No args constructor for use in serialization
     *
     */
    public Movie() {
    }

    /**
     *
     * @param budget
     * @param genres
     * @param spokenLanguages
     * @param runtime
     * @param backdropPath
     * @param voteCount
     * @param id
     * @param title
     * @param releaseDate
     * @param posterPath
     * @param originalTitle
     * @param voteAverage
     * @param video
     * @param popularity
     * @param revenue
     * @param productionCountries
     * @param status
     * @param originalLanguage
     * @param adult
     * @param imdbId
     * @param homepage
     * @param overview
     * @param belongsToCollection
     * @param productionCompanies
     * @param tagline
     * @param credits
     */
    public Movie(Boolean adult, String backdropPath, Collection belongsToCollection, Integer budget, RealmList<Genre> genres, String homepage, Integer id, String imdbId, String originalLanguage, String originalTitle, String overview, Double popularity, String posterPath, RealmList<ProductionCompany> productionCompanies, RealmList<ProductionCountry> productionCountries, String releaseDate, Integer revenue, Integer runtime, RealmList<SpokenLanguage> spokenLanguages, String status, String tagline, String title, Boolean video, Double voteAverage, Integer voteCount, Credits credits) {
        super();
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.belongsToCollection = belongsToCollection;
        this.budget = budget;
        this.genres = genres;
        this.homepage = homepage;
        this.id = id;
        this.imdbId = imdbId;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.productionCompanies = productionCompanies;
        this.productionCountries = productionCountries;
        this.releaseDate = releaseDate;
        this.revenue = revenue;
        this.runtime = runtime;
        this.spokenLanguages = spokenLanguages;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.credits = credits;
    }




    /*Fast Adapter implementation*/

    @Override
    public int getType() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Cinematograph.getContext());

        switch (sharedPreferences.getString("movie_layout","v_list_poster")) {
            case "v_list_poster":
                return R.id.card_movie_poster;
            case "v_list_backdrop":
                return R.id.card_movie_backdrop;
            case "v_list_grid":
                return R.id.card_movie_grid;
            case "h_list_poster":
                return R.id.card_movie_h_poster;
            default:
                throw new IllegalArgumentException("Invalid item layout");
        }
    }

    @Override
    public int getLayoutRes() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Cinematograph.getContext());

        switch (sharedPreferences.getString("movie_layout","v_list_poster")) {
            case "v_list_poster":
                return R.layout.card_movie_poster;
            case "v_list_backdrop":
                return R.layout.card_movie_backdrop;
            case "v_list_grid":
                return R.layout.card_movie_grid;
            case "h_list_poster":
                return R.layout.card_movie_h_poster;
            default:
                throw new IllegalArgumentException("Invalid item layout");
        }
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Cinematograph.getContext());

        holder.title.setText(title);

        Picasso.with(holder.itemView.getContext())
                .load(Cinematograph.getInstance().getString(R.string.image_base) + Cinematograph.getInstance().getString(R.string.backdrop_size_medium) + backdropPath)
                .into(holder.backdrop);

        switch (sharedPreferences.getString("movie_layout","v_list_poster")) {
            case "v_list_poster":
                holder.ratingBar.setRating((float) ((voteAverage * 5.0f) / 10.0f));
                holder.year.setText(releaseDate);
                break;
            case "v_list_backdrop":
                holder.ratingBar.setRating((float) ((voteAverage * 5.0f) / 10.0f));
                holder.year.setText(releaseDate);
                break;
            case "v_list_grid":
                holder.ratingBar.setRating((float) ((voteAverage * 5.0f) / 10.0f));
                break;
            case "h_list_poster":
                holder.overview.setText(overview);
                holder.rating.setText(holder.itemView.getContext().getString(R.string.rating_template, voteAverage));
                break;
            default:
                throw new IllegalArgumentException("Invalid item layout");
        }

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView backdrop;
        TextView title, year, overview, rating;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Cinematograph.getContext());

            backdrop = (ImageView) itemView.findViewById(R.id.backdrop);
            title = (TextView) itemView.findViewById(R.id.title);
            year = (TextView) itemView.findViewById(R.id.year);

            Log.e("MOVIE.JAVA:","(data:) " + sharedPreferences.getString("movie_layout","def val"));

            switch (sharedPreferences.getString("movie_layout", "v_list_poster")) {
                case "v_list_poster":
                    ratingBar = (RatingBar) itemView.findViewById(R.id.voteAverage);
                    break;
                case "v_list_backdrop":
                    ratingBar = (RatingBar) itemView.findViewById(R.id.voteAverage);
                    break;
                case "v_list_grid":
                    ratingBar = (RatingBar) itemView.findViewById(R.id.voteAverage);
                    break;
                case "h_list_poster":
                    rating = (TextView) itemView.findViewById(R.id.rating);
                    overview = (TextView) itemView.findViewById(R.id.overview);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid item layout");
            }

        }
    }

    //the static ViewHolderFactory which will be used to generate the ViewHolder for this Item
    private static final ViewHolderFactory<? extends Movie.ViewHolder> FACTORY = new Movie.ItemFactory();

    /**
     * our ItemFactory implementation which creates the ViewHolder for our adapter.
     * It is highly recommended to implement a ViewHolderFactory as it is 0-1ms faster for ViewHolder creation,
     * and it is also many many times more efficient if you define custom listeners on views within your item.
     */
    protected static class ItemFactory implements ViewHolderFactory<Movie.ViewHolder> {
        public Movie.ViewHolder create(View v) {
            return new Movie.ViewHolder(v);
        }
    }

    /**
     * return our ViewHolderFactory implementation here
     *
     * @return
     */
    @Override
    public ViewHolderFactory<? extends Movie.ViewHolder> getFactory() {
        return FACTORY;
    }





    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public Movie withAdult(Boolean adult) {
        this.adult = adult;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Movie withBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public Collection getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(Collection belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public Movie withBelongsToCollection(Collection belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
        return this;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public Movie withBudget(Integer budget) {
        this.budget = budget;
        return this;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(RealmList<Genre> genres) {
        this.genres = genres;
    }

    public Movie withGenres(RealmList<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Movie withHomepage(String homepage) {
        this.homepage = homepage;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Movie withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Movie withImdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Movie withOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Movie withOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Movie withOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Movie withPopularity(Double popularity) {
        this.popularity = popularity;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Movie withPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public RealmList<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(RealmList<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public Movie withProductionCompanies(RealmList<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
        return this;
    }

    public RealmList<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(RealmList<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public Movie withProductionCountries(RealmList<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Movie withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Movie withRevenue(Integer revenue) {
        this.revenue = revenue;
        return this;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Movie withRuntime(Integer runtime) {
        this.runtime = runtime;
        return this;
    }

    public RealmList<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(RealmList<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public Movie withSpokenLanguages(RealmList<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Movie withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Movie withTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Movie withTitle(String title) {
        this.title = title;
        return this;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Movie withVideo(Boolean video) {
        this.video = video;
        return this;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Movie withVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Movie withVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public Credits getCredits() {
        return credits;
    }

    public void setCredits(Credits credits) {
        this.credits = credits;
    }

    public Movie withCredits(Credits credits) {
        this.credits = credits;
        return this;
    }

}


