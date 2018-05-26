package com.example.huynhat.gettext3.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.huynhat.gettext3.Models.Post;
import com.example.huynhat.gettext3.Models.User;
import com.example.huynhat.gettext3.Models.WishList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by huynhat on 2017-11-11.
 * Refererence: http://www.androidtutorialshub.com/android-login-and-register-with-sqlite-database-tutorial/
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database version
    public static final int DATABASE_VERSION = 1;
    //Database Name
    public static final String DATABASE_NAME = "GetTextBookDB.db";

    //USERS Table
    public static final String TABLE_USER = "users";
    //--------COLS
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_NAME = "user_name";
    public static final String COL_USER_EMAIL = "user_email";
    public static final String COL_USER_PASSWORD = "user_password";


    //POSTS Table
    public static final String TABLE_POST ="posts";
    //-------------------COLS
    public static final String COL_POST_ID = "post_id";
    public static final String COL_BOOK_TITLE ="book_title";
    public static final String COL_ISBN ="isbn";
    public static final String COL_IMAGE ="image";
    public static final String COL_COURSE_USED ="course_used";
    public static final String COL_CONDITION ="condition";
    public static final String COL_PRICE ="price";

    //public static final String COL_DATETIME="timestamp";

    ///---------------- WISH_ILIST TABLE
    public static final String TABLE_WISH="wishs";
    public static final String COL_WISH_ID = "wish_id";


    //Create TABLE query
    private String CREATE_USER_TABLE ="CREATE TABLE " + TABLE_USER +"("
            +COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COL_USER_NAME+ " TEXT,"
            +COL_USER_EMAIL +" TEXT,"
            +COL_USER_PASSWORD + " TEXT)";

    private String CREATE_POST_TABLE ="CREATE TABLE " +TABLE_POST + " ("
            +COL_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COL_USER_EMAIL+ " TEXT,"
            +COL_IMAGE+ " TEXT,"
            +COL_BOOK_TITLE+ " TEXT,"
            +COL_ISBN +" TEXT,"
            +COL_COURSE_USED +" TEXT,"
            +COL_CONDITION  +" TEXT,"
            +COL_PRICE + " REAL )";

    private String CREATE_WISH_TABLE ="CREATE TABLE " +TABLE_WISH + " ("
            +COL_WISH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COL_POST_ID+ " INTEGER,"
            +COL_USER_EMAIL+ " TEXT)";

    //DROP table query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS "+TABLE_USER;
    private String DROP_POST_TABLE = "DROP TABLE IF EXISTS "+TABLE_POST;
    private String DROP_WISH_TABLE = "DROP TABLE IF EXISTS "+TABLE_WISH;

    /**
     * Constructor
     * @param context
     *
     */

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_POST_TABLE);
        sqLiteDatabase.execSQL(CREATE_WISH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //If "user" table exits --> DROP
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        sqLiteDatabase.execSQL(DROP_POST_TABLE);
        sqLiteDatabase.execSQL(DROP_WISH_TABLE);

        //Create tables again
        onCreate(sqLiteDatabase);
    }

    //-------------------------------------------------------

    public void addWishList(WishList wishList){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_POST_ID, wishList.getPost_id());
        values.put(COL_USER_EMAIL, wishList.getUser_email());

        //Inserting row
        db.insert(TABLE_WISH, null,values);
        db.close();
    }

    public long deleteWish(int wish_id){
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            return db.delete(TABLE_WISH, COL_WISH_ID +" =?",
                    new String[] {String.valueOf(wish_id)});
        }catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }




    //This method is to fetch all wishlist for a user
    public List<Post> getPostsWishlistForAuser(String userEmail){

        String innerJoinQuery= "SELECT posts."+COL_POST_ID+ ", posts."+COL_USER_EMAIL+", posts."+COL_IMAGE
                +", posts."+COL_BOOK_TITLE+", posts."+COL_ISBN+", posts."+COL_COURSE_USED
                +", posts."+COL_CONDITION+", posts."+COL_PRICE + " FROM wishs "
                +"INNER JOIN posts ON wishs."+COL_POST_ID+ " = posts."+COL_POST_ID
                +" INNER JOIN users ON wishs."+COL_USER_EMAIL+ " = users."+COL_USER_EMAIL
                +" WHERE wishs."+COL_USER_EMAIL+" =?";

        List<Post> postList = new ArrayList<Post>();
        SQLiteDatabase db = this.getReadableDatabase();//from database

        Cursor cursor = db.rawQuery(innerJoinQuery,new String[]{userEmail});

        //Traversing through all rows and adding to LIST ARRAY

        if(cursor.moveToFirst()){
            do{
                Post post = new Post();
                post.setPost_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_POST_ID))));
                post.setUser_email(cursor.getString(cursor.getColumnIndex(COL_USER_EMAIL)));
                post.setImage(cursor.getString(cursor.getColumnIndex(COL_IMAGE)));
                post.setBookTitle(cursor.getString(cursor.getColumnIndex(COL_BOOK_TITLE)));
                post.setIsbn(cursor.getString(cursor.getColumnIndex(COL_ISBN)));
                post.setCourseUsed(cursor.getString(cursor.getColumnIndex(COL_COURSE_USED)));
                post.setCondition(cursor.getString(cursor.getColumnIndex(COL_CONDITION)));
                post.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(COL_PRICE))));


                //Adding user record to arrayList
                postList.add(post);

            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return postList;
    }//END OF GETALLUSER LIST
    //-------------------------------------------------------

    public void addPost(Post post){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL, post.getUser_email());
        values.put(COL_IMAGE, post.getImage());
        values.put(COL_BOOK_TITLE, post.getBookTitle());
        values.put(COL_ISBN, post.getIsbn());
        values.put(COL_COURSE_USED, post.getCourseUsed());
        values.put(COL_CONDITION, post.getCondition());
        values.put(COL_PRICE, post.getPrice());

        //Inserting row
        db.insert(TABLE_POST, null,values);
        db.close();
    }

    //This method is to fetch all books
    public List<Post> getAllBooks(){
        //Array of cols to fetch
        String[] cols ={COL_POST_ID,COL_USER_EMAIL,COL_IMAGE,COL_BOOK_TITLE,
                COL_ISBN,COL_COURSE_USED,COL_CONDITION,COL_PRICE};

        //Sorting orders
        String sortOrder = COL_BOOK_TITLE + " ASC";

        List<Post> postList = new ArrayList<Post>();
        SQLiteDatabase db = this.getReadableDatabase();//from database

        Cursor cursor = db.query(
                TABLE_POST, //table to query
                cols,       //Columns to return
                null, //The columns for the WHERE clause
                null, //The VALUES for the WHERE clause
                null,      //group the rows
                null,       //filter by row groups
                sortOrder);         //Sort orders

        //Traversing through all rows and adding to LIST ARRAY
        if(cursor.moveToFirst()){
            do{
                Post post = new Post();
                post.setPost_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_POST_ID))));
                post.setUser_email(cursor.getString(cursor.getColumnIndex(COL_USER_EMAIL)));
                post.setImage(cursor.getString(cursor.getColumnIndex(COL_IMAGE)));
                post.setBookTitle(cursor.getString(cursor.getColumnIndex(COL_BOOK_TITLE)));
                post.setIsbn(cursor.getString(cursor.getColumnIndex(COL_ISBN)));
                post.setCourseUsed(cursor.getString(cursor.getColumnIndex(COL_COURSE_USED)));
                post.setCondition(cursor.getString(cursor.getColumnIndex(COL_CONDITION)));
                post.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(COL_PRICE))));


                //Adding user record to arrayList
                postList.add(post);

            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return postList;
    }//END OF GETALLUSER LIST


    //This method is to fetch all books POSTED BY A USER
    public List<Post> getMyPosts(String userEmail){
        //Array of cols to fetch
        String[] cols ={COL_POST_ID,COL_USER_EMAIL,COL_IMAGE,COL_BOOK_TITLE,
                COL_ISBN,COL_COURSE_USED,COL_CONDITION,COL_PRICE};
        String selection = COL_USER_EMAIL + " =?";
        String[] selectionArgs = {userEmail};


        //Sorting orders
        String sortOrder = COL_BOOK_TITLE + " ASC";

        List<Post> postList = new ArrayList<Post>();
        SQLiteDatabase db = this.getReadableDatabase();//from database

        Cursor cursor = db.query(
                TABLE_POST, //table to query
                cols,       //Columns to return
                selection, //The columns for the WHERE clause
                selectionArgs, //The VALUES for the WHERE clause
                null,      //group the rows
                null,       //filter by row groups
                sortOrder);         //Sort orders

        //Traversing through all rows and adding to LIST ARRAY
        if(cursor.moveToFirst()){
            do{
                Post post = new Post();
                post.setPost_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_POST_ID))));
                post.setUser_email(cursor.getString(cursor.getColumnIndex(COL_USER_EMAIL)));
                post.setImage(cursor.getString(cursor.getColumnIndex(COL_IMAGE)));
                post.setBookTitle(cursor.getString(cursor.getColumnIndex(COL_BOOK_TITLE)));
                post.setIsbn(cursor.getString(cursor.getColumnIndex(COL_ISBN)));
                post.setCourseUsed(cursor.getString(cursor.getColumnIndex(COL_COURSE_USED)));
                post.setCondition(cursor.getString(cursor.getColumnIndex(COL_CONDITION)));
                post.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex(COL_PRICE))));


                //Adding user record to arrayList
                postList.add(post);

            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return postList;
    }//END OF GETALLUSER LIST


    public long updatePostRecord (int post_num, String newCourse, double newPrice, String newCondition){
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            values.put(COL_COURSE_USED, newCourse);
            values.put(COL_PRICE, newPrice);
            values.put(COL_CONDITION, newCondition);

            return db.update(TABLE_POST,values,
                    COL_POST_ID+ " =?", new String[] {String.valueOf(post_num)});

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public long deletePost(int post_num){
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            return db.delete(TABLE_POST, COL_POST_ID +" =?",
                    new String[] {String.valueOf(post_num)});
        }catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Retrieve post ID from the database
     * @param post
     */


    public Cursor getPostID(Post post){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_POST
                + " WHERE "+COL_USER_EMAIL + " = '" + post.getUser_email()+"'";
        return db.rawQuery(sql,null);

    }

    //===============================================================================================

    //Add user record
    public void addUser (User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, user.getName());
        values.put(COL_USER_EMAIL, user.getEmail());
        values.put(COL_USER_PASSWORD, user.getPassword());

        //Inserting row
        db.insert(TABLE_USER,null,values);
        db.close();
    }


    //This method is to fetch all users and return the list of user records
    public List<User> getAllUser(){
        //Array of cols to fetch
        String[] cols ={COL_USER_ID,COL_USER_NAME,COL_USER_EMAIL,COL_USER_PASSWORD};

        //Sorting orders
        String sortOrder = COL_USER_NAME + " ASC";

        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();//from database
         /*
         "SELECT user_id, user_name, user_email, user_password FROM users ORDER BY user_name"
          */
        Cursor cursor = db.query(
                TABLE_USER, //table to query
                cols,       //Columns to return
                null, //The columns for the WHERE clause
                null, //The VALUES for the WHERE clause
                null,      //group the rows
                null,       //filter by row groups
                sortOrder);         //Sort orders

        //Traversing through all rows and adding to LIST ARRAY
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COL_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COL_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COL_USER_PASSWORD)));

                //Adding user record to arrayList
                userList.add(user);

            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return userList;
    }//END OF GETALLUSER LIST



    //UPDATE USER RECORD
    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, user.getName());
        values.put(COL_USER_EMAIL, user.getEmail());
        values.put(COL_USER_PASSWORD, user.getPassword());

        //Updating row
        db.update(TABLE_USER, values, COL_USER_ID +" =?",
                new String[]{String.valueOf(user.getId())});

        db.close();
    }

    //Delete user record
    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        //delete record by id
        db.delete(TABLE_USER, COL_USER_ID +" =?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //Checking if user email exists or NOT for the Login activity
    public boolean checkUser(String email){
        //Array of cols to fetch
        String[] cols ={COL_USER_ID};
        //Selection Criteria
        String selection = COL_USER_EMAIL + " = ? ";
        //Selection Argument
        String[] selectionArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER,cols,selection, selectionArgs
                ,null,null,null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount>0){
            return true;
        }
        return false;
    }

    /**
     * To be used when user is trying to regsiter for new account || Checking if email exists
     * @param email
     * @param password
     * @return true if there is an email alreay in database
     */
    //Checking if email entered has been register?
    public boolean checkUser(String email, String password){
        //Array cols to fetch
        String[] cols ={COL_USER_ID};
        String selection = COL_USER_EMAIL + " = ?" + " AND " + COL_USER_PASSWORD + " = ? ";
        String[] selectionArgs = {email,password};


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, cols, selection, selectionArgs
                ,null,null,null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount>0){
            return true; // meaning there is an email already
        }
        return false;

    }

    //Get myFav Posts



    //Get UserID
    public Cursor getUserID (User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM "+ TABLE_USER +
                " WHERE "+ COL_USER_ID + " = '"+user.getEmail()+"'";
        return db.rawQuery(sql,null);
    }

    //Return User Actual Name:
    public String getActualName(String email) throws SQLException {
        String[] cols = {COL_USER_NAME};
        String selection = COL_USER_EMAIL + " =?";
        String[] selectionArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, cols, selection, selectionArgs,
                null, null, null);

        String userName = "";
        if(cursor.moveToFirst()){
            do{
                userName =cursor.getString(0);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return userName;

    }
    //Return UserID
    public int getActualID(String email) throws SQLException {
        String[] cols = {COL_USER_ID};
        String selection = COL_USER_EMAIL + " =?";
        String[] selectionArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, cols, selection, selectionArgs,
                null, null, null);

        int userID = 0;
        if(cursor.moveToFirst()){
            do{
                userID =cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return userID;

    }

    /**
     * Get datetime when posting
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

     
}
