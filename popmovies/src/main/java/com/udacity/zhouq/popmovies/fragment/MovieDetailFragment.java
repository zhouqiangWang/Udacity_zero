package com.udacity.zhouq.popmovies.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.udacity.zhouq.popmovies.R;
import com.udacity.zhouq.popmovies.adapter.MovieItem;
import com.udacity.zhouq.popmovies.adapter.NetworkUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {
  private final String TAG = MovieDetailFragment.class.getSimpleName();
  // the fragment initialization parameters, e.g. ARG_ITEM
  private static final String ARG_ITEM = "movie_item";

  private MovieItem mItem;

  private OnFragmentInteractionListener mListener;

  public MovieDetailFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param item Parameter 1.
   * @return A new instance of fragment MovieDetailFragment.
   */
  public static MovieDetailFragment newInstance(MovieItem item) {
    MovieDetailFragment fragment = new MovieDetailFragment();
    Bundle args = new Bundle();
    args.putParcelable(ARG_ITEM, item);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mItem = getArguments().getParcelable(ARG_ITEM);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View root = inflater.inflate(R.layout.fragment_movie_detail, container, false);

    Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
    ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    toolbar.setTitle(R.string.title_fragment_detail);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setHasOptionsMenu(true);

    DraweeView poster = (DraweeView) root.findViewById(R.id.image_poster);
    Uri postUri = Uri.parse(NetworkUtils.TMDB_IMAGE_PREFIX + mItem.poster);
    Log.e(TAG, "onCreateView: uri = "+ postUri);
    DraweeController controller = Fresco.newDraweeControllerBuilder()
        .setUri(postUri)
        .setTapToRetryEnabled(true)
        .setOldController(poster.getController())
        .build();
    poster.setController(controller);

    TextView tvTitle = (TextView) root.findViewById(R.id.title);
    tvTitle.setText(mItem.title);
    TextView tvRleasetime = (TextView) root.findViewById(R.id.release_date);
    tvRleasetime.setText(mItem.releaseDate);
    TextView tvVoteRates = (TextView) root.findViewById(R.id.vote_average);
    tvVoteRates.setText(String.format(getString(R.string.vote_average), mItem.voteRates));
    TextView overView = (TextView) root.findViewById(R.id.over_view);
    overView.setText(mItem.overview);
    final TextView tvRuntime = (TextView) root.findViewById(R.id.runtime);

    String url = NetworkUtils.getInstance(getContext()).buildURLWithMovieID(mItem.id);
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
        new Response.Listener<JSONObject>() {
          @Override public void onResponse(JSONObject response) {
            try {
              String minutes = response.getString("runtime");
              if(minutes != null && !minutes.equals("0")){
                tvRuntime.setText(String.format(getString(R.string.run_time), minutes));
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        }, new Response.ErrorListener() {
          @Override public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "onErrorResponse: " + error);
          }
        });
    NetworkUtils.getInstance(getContext()).getVolleyQueue().add(jsonObjectRequest);
    return root;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }
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
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
