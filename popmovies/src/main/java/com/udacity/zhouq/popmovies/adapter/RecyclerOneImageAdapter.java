package com.udacity.zhouq.popmovies.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.udacity.zhouq.popmovies.R;
import com.udacity.zhouq.popmovies.fragment.MovieListFragment.OnListFragmentInteractionListener;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link String} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class RecyclerOneImageAdapter extends RecyclerView.Adapter<RecyclerOneImageAdapter.ViewHolder> {

  private final List<MovieItem> mData;
  private final OnListFragmentInteractionListener mListener;

  public RecyclerOneImageAdapter(List<MovieItem> items, OnListFragmentInteractionListener listener) {
    mData = items;
    mListener = listener;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_grid_image, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
    final MovieItem item = mData.get(position);
    holder.url = NetworkUtils.TMDB_IMAGE_PREFIX + item.poster;
    Uri uri = Uri.parse(holder.url);
    DraweeController controller = Fresco.newDraweeControllerBuilder()
        .setUri(uri)
        .setTapToRetryEnabled(true)
        .setOldController(holder.image.getController())
        .build();
    holder.image.setController(controller);

    holder.mItemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (null != mListener) {
          // Notify the active callbacks interface (the activity, if the
          // fragment is attached to one) that an item has been selected.
          mListener.onListFragmentInteraction(item);
        }
      }
    });
  }

  @Override public int getItemCount() {
    return mData.size();
  }


  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mItemView;
    public final DraweeView image;
    public String url;

    public ViewHolder(View view) {
      super(view);
      mItemView = view;
      image = (DraweeView) view.findViewById(R.id.image);
    }
  }
}
