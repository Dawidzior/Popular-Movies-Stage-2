package dawidzior.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dawidzior.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>,
        MoviesListAdapter.MovieClickListener {

    public static final String SORT_BY_KEY = "SORT_BY_KEY";
    public static final String SHARED_ELEMENT = "sharedElement";
    private static final String SORT_BY_POPULARITY = "popular";
    private static final String SORT_BY_RATING = "top_rated";
    private static final int MOVIES_LIST_LOADER_ID = 1;
    @BindView(R.id.movies_list)
    protected RecyclerView moviesList;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.progress_bar)
    protected ProgressBar progressBar;
    @BindView(R.id.error_empty_text)
    protected TextView errorEmptyText;
    //TODO will ContentObserver fit my needs?
    private boolean lastRequestFromDatabase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer
                .span_count));
        moviesList.setLayoutManager(gridLayoutManager);

        //Popularity is default sorting option.
        requestData(SORT_BY_POPULARITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //If user is coming back from details (using favourites sort option) reload the list.
        if (lastRequestFromDatabase) {
            requestData(null);
        }
    }

    private void requestData(String internetRequestSortByKey) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            showProgress();
            Bundle queryBundle = new Bundle();
            queryBundle.putString(SORT_BY_KEY, internetRequestSortByKey);

            if (getLoaderManager().getLoader(MOVIES_LIST_LOADER_ID) != null) {
                getLoaderManager().restartLoader(MOVIES_LIST_LOADER_ID, queryBundle, this).forceLoad();
            } else {
                getLoaderManager().initLoader(MOVIES_LIST_LOADER_ID, queryBundle, this).forceLoad();
            }
        } else {
            showError();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_by_popularity) {
            requestData(SORT_BY_POPULARITY);
            lastRequestFromDatabase = false;
            return true;
        } else if (id == R.id.sort_by_rating) {
            requestData(SORT_BY_RATING);
            lastRequestFromDatabase = false;
            return true;
        } else if (id == R.id.sort_by_favourites) {
            requestData(null);
            lastRequestFromDatabase = true;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new MoviesListLoader(this, bundle);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        if (!movies.isEmpty()) {
            MoviesListAdapter adapter = new MoviesListAdapter(this, movies, this);
            //API returns always 20 items.
            moviesList.setHasFixedSize(true);
            moviesList.setAdapter(adapter);
            showList();
        } else {
            showEmptyText();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
    }

    private void showProgress() {
        errorEmptyText.animate().alpha(0f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        errorEmptyText.setVisibility(View.GONE);
        crossfadeAnimation(moviesList, progressBar);
    }

    private void showError() {
        errorEmptyText.setText(R.string.download_error);
        crossfadeAnimation(progressBar, errorEmptyText);
    }

    private void showEmptyText() {
        errorEmptyText.setText(R.string.empty_list);
        crossfadeAnimation(progressBar, errorEmptyText);
    }

    private void showList() {
        crossfadeAnimation(progressBar, moviesList);
    }

    private void crossfadeAnimation(View out, View in) {
        in.setAlpha(0f);
        in.setVisibility(View.VISIBLE);
        in.animate().alpha(1f).setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));

        if (out.getVisibility() == View.VISIBLE) {
            out.animate().alpha(0f).setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        }
    }

    @Override
    public void onClick(Movie movie, View sharedView) {
        Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
        detailsIntent.putExtra(DetailsActivity.MOVIE_BUNDLE_KEY, movie);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                sharedView, SHARED_ELEMENT);
        startActivity(detailsIntent, options.toBundle());
    }
}
