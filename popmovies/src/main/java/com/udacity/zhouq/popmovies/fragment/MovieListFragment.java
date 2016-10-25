package com.udacity.zhouq.popmovies.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.udacity.zhouq.popmovies.R;
import com.udacity.zhouq.popmovies.adapter.MovieItem;
import com.udacity.zhouq.popmovies.adapter.NetworkUtils;
import com.udacity.zhouq.popmovies.adapter.RecyclerOneImageAdapter;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a list of Items.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MovieListFragment extends Fragment {

  private final String TAG = MovieListFragment.class.getSimpleName()+"-wang";

  private OnListFragmentInteractionListener mListener;

  private ArrayList<MovieItem> mData;
  private final String KEY_DATA = "data";
  private boolean needLoad = true;
  private final String KEY_NEED_LOAD = "need_load";
  private RecyclerView.Adapter mAdapter;
  private String mLanguage = NetworkUtils.TMDB_LANGUAGE_ZH;
  private String mCategory;
  private int mPage;

  private Menu mMenu;

  private int[] mLastVisibleItemPositions;
  private int mLastVisiblePosition = 0;
  private boolean hasScrolled;
  private boolean isLoading;
  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public MovieListFragment() {
    mData = new ArrayList<>();
    needLoad = true;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnListFragmentInteractionListener) {
      mListener = (OnListFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
    }

  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if(savedInstanceState != null){
      needLoad = savedInstanceState.getBoolean(KEY_NEED_LOAD);
      mData = savedInstanceState.getParcelableArrayList(KEY_DATA);
    }
    setHasOptionsMenu(true);

    if(needLoad) {
      fetchNewCategoryMovieList(NetworkUtils.TMDB_CATEGORY_POP);
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    outState.putBoolean(KEY_NEED_LOAD, needLoad);
    outState.putParcelableArrayList(KEY_DATA, mData);
    super.onSaveInstanceState(outState);
  }

  private void fetchNewCategoryMovieList(String category){
    mData.clear();
    mCategory = category;
    mPage = 1;

    fetchMovieList(mLanguage, category, mPage);
  }

  private void fetchNextPageMovieList(){
    mPage++;
    fetchMovieList(mLanguage, mCategory, mPage);
  }
  private void fetchMovieList(String language, String category, int page){
    mLanguage = language;
    mCategory = category;
    mPage = page;
    isLoading = true;

    String url = NetworkUtils.getInstance(getContext()).buildURLWithPage(category, language, page);
    JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
        new com.android.volley.Response.Listener<JSONObject>() {
          @Override public void onResponse(JSONObject response) {
            Log.d(TAG, "onResponse: "+response);
            try {
              JSONArray results = response.getJSONArray("results");
              for(int i = 0; i<results.length(); i++){
                JSONObject obj = results.getJSONObject(i);
                MovieItem item = new MovieItem();
                item.poster = obj.getString("poster_path");
                item.id = obj.getString("id");
                item.title = obj.getString("title");
                item.overview = obj.getString("overview");
                item.releaseDate = obj.getString("release_date");
                item.voteRates = obj.getString("vote_average");
                mData.add(item);
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
            isLoading = false;
            needLoad = false;
          }
        }, new com.android.volley.Response.ErrorListener() {
      @Override public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "onErrorResponse: " + error);
        isLoading = false;
      }
    });
    NetworkUtils.getInstance(getContext()).getVolleyQueue().add(jsonObjRequest);
  }

  @RequiresApi(api = Build.VERSION_CODES.M) @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

    Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    Spinner category = (Spinner) view.findViewById(R.id.category_spinner);
    category.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
          case 0:
            fetchNewCategoryMovieList(NetworkUtils.TMDB_CATEGORY_POP);
            break;
          case 1:
            fetchNewCategoryMovieList(NetworkUtils.TMDB_CATEGORY_TOP_RATED);
            break;
        }
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    View recycler = view.findViewById(R.id.list);
    // Set the adapter
    if (recycler instanceof RecyclerView) {
      RecyclerView recyclerView = (RecyclerView) recycler;

      recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

      mAdapter = new RecyclerOneImageAdapter(mData, mListener);
      recyclerView.setAdapter(mAdapter);
      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
          Log.d(TAG, "onScrollStateChanged: newState = "+newState);
          switch (newState){
            case RecyclerView.SCROLL_STATE_IDLE:
              StaggeredGridLayoutManager layoutManager =
                  (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
              int visibleCount = layoutManager.getChildCount();
              int totalCount = layoutManager.getItemCount();

              if(mLastVisibleItemPositions == null){
                mLastVisibleItemPositions = new int[layoutManager.getSpanCount()];
              }
              mLastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(
                  mLastVisibleItemPositions);
              mLastVisiblePosition = mLastVisibleItemPositions[0];
              for (int i : mLastVisibleItemPositions) {
                mLastVisiblePosition = Math.max(mLastVisiblePosition, i);
              }

              if(visibleCount > 0 && hasScrolled && mLastVisiblePosition == totalCount-1 && !isLoading){
                fetchNextPageMovieList();
              }
              break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
              hasScrolled = false;
              break;
          }
        }

        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          hasScrolled = true;
        }
      });
    }
    return view;
  }

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnListFragmentInteractionListener {

    void onListFragmentInteraction(MovieItem item);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    //mMenu = menu;
    //inflater.inflate(R.menu.menu_main, menu);
    //super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    switch (id){
      case R.id.sort_pop:
        fetchNewCategoryMovieList(NetworkUtils.TMDB_CATEGORY_POP);
        mMenu.findItem(R.id.sort_rated).setVisible(true);
        mMenu.findItem(R.id.sort_pop).setVisible(false);
        return true;
      case R.id.sort_rated:
        fetchNewCategoryMovieList(NetworkUtils.TMDB_CATEGORY_TOP_RATED);
        mMenu.findItem(R.id.sort_rated).setVisible(false);
        mMenu.findItem(R.id.sort_pop).setVisible(true);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
