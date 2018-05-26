package com.example.huynhat.gettext3.Utils;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huynhat.gettext3.Models.Post;
import com.example.huynhat.gettext3.Models.WishList;
import com.example.huynhat.gettext3.R;
import com.example.huynhat.gettext3.SQL.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huynhat on 2017-11-14.
 * Reference:
 */

public class RecyclerAdapterWish extends RecyclerView.Adapter<RecyclerAdapterWish.AppViewHolder> implements Filterable {

    //private List<User> listUsers;
    private Context context;
    private List<Post> listPost;
    private List<Post> listPostFiltered;

    private SessionManagement sessionManagement;
    private DatabaseHelper databaseHelper;

    String userEmail;
    WishList wishList;

    public RecyclerAdapterWish(List<Post> listPost) {
        this.listPost = listPost;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookrow_recycler_item_layout, parent, false);
        context =parent.getContext();
        /*
        sessionManagement = new SessionManagement(context);
        databaseHelper = new DatabaseHelper(context);
        userEmail = sessionManagement.getUserEmail();

        */

        return new AppViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AppViewHolder holder, final int position) {

        sessionManagement = new SessionManagement(context);
        databaseHelper = new DatabaseHelper(context);
        userEmail = sessionManagement.getUserEmail();

        //holder.bookCover.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile()));
        holder.bookTitle.setText(listPost.get(position).getBookTitle());
        holder.isbn.setText(listPost.get(position).getIsbn());
        holder.course.setText(listPost.get(position).getCourseUsed());
        holder.price.setText(String.valueOf(listPost.get(position).getPrice()));
        holder.condition.setText(listPost.get(position).getCondition());
        //Using pisccao for load image from database url
        Picasso.with(holder.bookCover.getContext()).load(listPost.get(position).getImage()).into(holder.bookCover);

        holder.btnThreeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_wish, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_remove_wishlist:
                                /*
                                WishList wishList = new WishList();
                                wishList.setPost_id(listPost.get(position).getPost_id());
                                wishList.setUser_email(userEmail);
                                databaseHelper.addWishList(wishList);
                                */
                                //databaseHelper.deleteWish(position);
                                Toast.makeText(context, "Remove From your wish list"+position, Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.action_email_user1:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("message/rfc822");
                                intent.putExtra(Intent.EXTRA_EMAIL,
                                        new String[]{listPost.get(position).getUser_email().toString()});

                                String reBookTitle= listPost.get(position).getBookTitle().toString();
                                String message= "Hi, \n\nI'd to buy '"+reBookTitle +"' that is posted on Get Text App. \n"
                                        +"Your asking price: $"+String.valueOf(listPost.get(position).getPrice())+ "\n"
                                        +"Can we meet at New West Campus?\n\nThanks,";

                                intent.putExtra(Intent.EXTRA_SUBJECT,"RE: Textbook: "+reBookTitle);
                                intent.putExtra(Intent.EXTRA_TEXT,message);

                                context.startActivity(Intent.createChooser(intent,"Send via..."));

                                break;

                            default:
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public void setFilter(ArrayList<Post> newPost){
        listPost = new ArrayList<>();
        listPost.addAll(newPost);
        notifyDataSetChanged();
    }

    public void setNoFilter(){
        listPost = listPost;
        listPost.addAll(listPost);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    listPostFiltered = listPost;
                }else {
                    List<Post> filterPost = new ArrayList<>();
                    for(Post post : listPost){
                        String courseNum = post.getCourseUsed().toLowerCase();
                        String name = post.getBookTitle().toLowerCase();
                        String isbnFromDatabase = post.getIsbn();
                        if (name.contains(charString.toLowerCase())
                                || isbnFromDatabase.contains(charString.toLowerCase())
                                || courseNum.contains(charString.toLowerCase())) {
                            filterPost.add(post);
                        }
                    }
                    listPostFiltered=filterPost;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listPostFiltered;

                return filterResults;
                }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listPost = (ArrayList<Post>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    /**
     * ViewHolder class
     */
    public class AppViewHolder extends RecyclerView.ViewHolder {
        public ImageView bookCover, btnThreeDots;
        public TextView bookTitle;
        public TextView isbn;
        public TextView course;
        public TextView price;
        public TextView condition;


        public AppViewHolder(View view) {
            super(view);
            bookCover =(ImageView) view.findViewById(R.id.bookImageView);
            btnThreeDots =(ImageView) view.findViewById(R.id.threeDots);
            bookTitle = (TextView) view.findViewById(R.id.bookTitleView);
            isbn = (TextView) view.findViewById(R.id.isbnView);
            course =(TextView) view.findViewById(R.id.courseView);
            price = (TextView) view.findViewById(R.id.priceView);
            condition = (TextView) view.findViewById(R.id.conditionView);
        }
    }



    /**
     * Click listener for popup menu items
     */

    /*
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_wishlist:
                    Toast.makeText(context, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.action_email_user:
                    Toast.makeText(context, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    */




}