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
import android.widget.Button;
import android.widget.TextView;

import com.example.huynhat.gettext3.Models.Post;
import com.example.huynhat.gettext3.SQL.DatabaseHelper;
import com.example.huynhat.gettext3.Utils.RecyclerAdapter;
import com.example.huynhat.gettext3.Utils.RecyclerAdapterMe;
import com.example.huynhat.gettext3.Utils.SessionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huynhat on 2017-11-16.
 */

public class FragmentProfile extends Fragment {

    private Context context;
    private Button signOutBtn;
    private TextView userNameTextview;
    private List<Post> myOwnPost;
    private SessionManagement sessionManagement;
    private DatabaseHelper databaseHelper;

    private RecyclerView mRecyclerView;
    private RecyclerAdapterMe mRecyclerAdapter;
    String userEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container,false);

        sessionManagement = new SessionManagement(this.getActivity());
        myOwnPost =  new ArrayList<>();

        databaseHelper = new DatabaseHelper(getContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerMyPost);

        mRecyclerAdapter = new RecyclerAdapterMe(myOwnPost);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerAdapter);


        signOutBtn = (Button) view.findViewById(R.id.btnSignOut);
        userNameTextview = (TextView) view.findViewById(R.id.userName);
        //Obtain user's email from sharedPref then query database for an actual name
        userEmail = sessionManagement.getUserEmail();
        String actualName=databaseHelper.getActualName(userEmail);
        userNameTextview.setText(actualName.toString());
        final int userID=databaseHelper.getActualID(userEmail);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManagement.logoutUser();
            }
        });

        getAllOfMyPosts(userEmail);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllOfMyPosts(userEmail);
    }

    /**
     * This method is to fetch all the books records from SQLite
     */
    private void getAllOfMyPosts(final String userEmail) {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                myOwnPost.clear();
                myOwnPost.addAll(databaseHelper.getMyPosts(userEmail));

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
