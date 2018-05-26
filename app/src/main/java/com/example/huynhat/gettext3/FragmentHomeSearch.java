package com.example.huynhat.gettext3;

import android.app.SearchManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;


import com.example.huynhat.gettext3.Models.Post;
import com.example.huynhat.gettext3.Models.User;
import com.example.huynhat.gettext3.SQL.DatabaseHelper;
import com.example.huynhat.gettext3.Utils.RecyclerAdapter;
import com.example.huynhat.gettext3.Utils.SessionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huynhat on 2017-11-16.
 */

public class FragmentHomeSearch extends Fragment  {
    Context context;
    private ImageView mFilter;
    private EditText searchField;

    private SearchView searchView;


    private RecyclerView mRecyclerView;
    private List<User> listUsers;
    private List<Post> listPosts;




    private RecyclerAdapter mRecyclerAdapter;
    private DatabaseHelper databaseHelper;
    private SessionManagement mSession;

    public FragmentHomeSearch() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_search, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewUsers);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        //initObjects();
        listPosts = new ArrayList<>();

        mRecyclerAdapter = new RecyclerAdapter(listPosts);


        mSession = new SessionManagement(this.getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        databaseHelper = new DatabaseHelper(getActivity());
        //addABook();
        getAllBooksFromSQLite();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                //mRecyclerAdapter.getFilter().filter(newText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*
                mRecyclerAdapter.getFilter().filter(newText);
                return false;
                */

                newText = newText.toLowerCase();

                if(!newText.equalsIgnoreCase("")){

                    ArrayList<Post> newList = new ArrayList<>();

                    for (Post post : listPosts) {
                        //Allow to search using book name || Course || ISBN
                        String courseNum = post.getCourseUsed().toLowerCase();
                        String name = post.getBookTitle().toLowerCase();
                        String isbnFromDatabase = post.getIsbn();
                        if (name.contains(newText) || isbnFromDatabase.contains(newText) ||courseNum.contains(newText)) {
                            newList.add(post);
                        }
                    }
                    mRecyclerAdapter.setFilter(newList);

                }else {
                    //mRecyclerAdapter.setNoFilter();
                }

                return false;
            }
        });// End of set of queryTextChanged


        if(!mSession.isLoggedIn()){
            mSession.logoutUser();
        }

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getAllBooksFromSQLite();

    }



    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        //listUsers = new ArrayList<>();

        listPosts = new ArrayList<>();

        mRecyclerAdapter = new RecyclerAdapter(listPosts);


        mSession = new SessionManagement(this.getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        databaseHelper = new DatabaseHelper(getActivity());
        getAllBooksFromSQLite();
    }


    /**
     * This method is to fetch all the books records from SQLite
     */
    private void getAllBooksFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listPosts.clear();
                listPosts.addAll(databaseHelper.getAllBooks());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }//end of getDataFromSQLite





    /*
    //Adding sample books
    public void addABook(){
        Post post = new Post();
        post.setUser_id(1);
        post.setBookTitle("Android BootCamp");
        post.setIsbn("123456");
        post.setPrice(19.00);
        post.setCondition("Good");
        post.setCourseUsed("CSIS3175");

        databaseHelper.addPost(post);
    }
    */





    /**
     * This method is to fetch all user records from SQLite
     */

    /*
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                listUsers.addAll(databaseHelper.getAllUser());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }//end of getDataFromSQLite

    */


    //Filter Actvity will be here
    /*
    private void init(){
        mFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);

            }
        });
    }
    */





}
