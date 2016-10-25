package com.udacity.zhouq.popmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.udacity.zhouq.popmovies.adapter.MovieItem;
import com.udacity.zhouq.popmovies.fragment.MovieDetailFragment;
import com.udacity.zhouq.popmovies.fragment.MovieListFragment;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnListFragmentInteractionListener, MovieDetailFragment.OnFragmentInteractionListener{

  private final String FRAGMENT_TAG_LIST = "movie_list_fragment";
  private final String FRAGMENT_TAG_DETAIL = "movie_detail_fragment";
  private Fragment mListFragment;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

    mListFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG_LIST);
    if(mListFragment == null){
      mListFragment = new MovieListFragment();
      trans.add(R.id.list_fragment, mListFragment, FRAGMENT_TAG_LIST);

      trans.commit();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    switch (item.getItemId()){
      case android.R.id.home:
        onBackPressed();
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onListFragmentInteraction(MovieItem item) {
    //Toast.makeText(this, item.title+" is clicked!", Toast.LENGTH_LONG).show();

    FragmentManager fm = getSupportFragmentManager();
    Fragment detailFragment = fm.findFragmentByTag(FRAGMENT_TAG_DETAIL);
    if(detailFragment == null){
      detailFragment = MovieDetailFragment.newInstance(item);
    }else{
      //TODO: trans MoviewItem into Detail Fragment
    }

    FragmentTransaction transaction = fm.beginTransaction();
    transaction.replace(R.id.list_fragment, detailFragment, FRAGMENT_TAG_DETAIL);
    transaction.addToBackStack(FRAGMENT_TAG_DETAIL);
    transaction.commit();
  }

  @Override public void onFragmentInteraction(Uri uri) {

  }
}
