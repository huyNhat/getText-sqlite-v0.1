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

import com.example.huynhat.gettext3.EditPostActivity;
import com.example.huynhat.gettext3.Models.Post;
import com.example.huynhat.gettext3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huynhat on 2017-11-14.
 * Reference:
 */

public class RecyclerAdapterMe extends RecyclerView.Adapter<RecyclerAdapterMe.AppViewHolder>
        implements Filterable {

    //private List<User> listUsers;
    private Context context;
    private List<Post> listPost;
    private List<Post> listPostFiltered;

    public RecyclerAdapterMe(List<Post> listPost) {
        this.listPost = listPost;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookrow_recycler_item_layout, parent, false);
        context =parent.getContext();
        return new AppViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AppViewHolder holder, final int position) {
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
                inflater.inflate(R.menu.edit_post_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_edit:
                                Intent intent = new Intent(context, EditPostActivity.class);
                                intent.putExtra("PostID", listPost.get(position).getPost_id());
                                intent.putExtra("UserEMAIL", listPost.get(position).getUser_email());
                                intent.putExtra("ImgURL", listPost.get(position).getImage());
                                intent.putExtra("BookTitle", listPost.get(position).getBookTitle());
                                intent.putExtra("ISBN", listPost.get(position).getIsbn());
                                intent.putExtra("Price", listPost.get(position).getPrice());
                                intent.putExtra("CourseUsed", listPost.get(position).getCourseUsed());
                                intent.putExtra("Condition", listPost.get(position).getCondition());
                                context.startActivity(intent);
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