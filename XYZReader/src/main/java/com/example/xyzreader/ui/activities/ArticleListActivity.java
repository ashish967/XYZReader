package com.example.xyzreader.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.UpdaterService;
import com.example.xyzreader.ui.fragments.ArticleListFragment;
import com.example.xyzreader.ui.utils.AppUtils;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    ArticleListFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppUtils.setFullScreen(this);

        setContentView(R.layout.activity_article_list);

        mFragment= new ArticleListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content,mFragment)
                .commit();



    }


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));

    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mRefreshingReceiver);
    }

    private boolean mIsRefreshing = false;

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);

                showNoInternetConnection(intent.getBooleanExtra(UpdaterService.ONLINE,true));
                updateRefreshingUI();
            }
        }
    };

    private void showNoInternetConnection(boolean online) {

        if(!online){

            mFragment.showNoInternetConnection();
        }

    }


    private void updateRefreshingUI() {


        mFragment.updateRefreshingUI(mIsRefreshing);



    }


}
