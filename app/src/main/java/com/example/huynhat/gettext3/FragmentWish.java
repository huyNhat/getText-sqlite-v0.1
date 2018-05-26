package com.example.huynhat.gettext3;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huynhat.gettext3.Models.Post;
import com.example.huynhat.gettext3.SQL.DatabaseHelper;
import com.example.huynhat.gettext3.Utils.RecyclerAdapterMe;
import com.example.huynhat.gettext3.Utils.RecyclerAdapterWish;
import com.example.huynhat.gettext3.Utils.SessionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huynhat on 2017-11-16.
 */

public class FragmentWish extends Fragment {
    private Context context;
    private DatabaseHelper databaseHelper;
    private List<Post> myWishList;
    private RecyclerView mRecyclerView;
    private RecyclerAdapterWish mRecyclerAdapter;
    private SessionManagement sessionManagement;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_wish, container,false);

        myWishList =  new ArrayList<>();

        databaseHelper = new DatabaseHelper(getContext());
        sessionManagement = new SessionManagement(this.getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerMyWishList);

        mRecyclerAdapter = new RecyclerAdapterWish(myWishList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        String userEmail = sessionManagement.getUserEmail();

        getMyWishLish(userEmail);
        return view;
    }
    /**
     * This method is to fetch all wish list from SQLite
     */
    private void getMyWishLish(final String userEmail) {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                myWishList.clear();
                myWishList.addAll(databaseHelper.getPostsWishlistForAuser(userEmail));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }//end of getDataFromSQLite
}
