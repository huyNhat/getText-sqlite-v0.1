package com.example.huynhat.gettext3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.huynhat.gettext3.SQL.DatabaseHelper;
import com.squareup.picasso.Picasso;

/**
 * Created by 300239275 on 11/26/2017.
 */

public class EditPostActivity extends AppCompatActivity {

    private Button btnSaveEdit, btnDeletePost;
    private ImageView bookCoverImageView, btnScanBarcode;
    private EditText titleBook, isbnNumber, price, course;
    private Spinner conditionSpinner;
    private String selectedImagePath, userEmail;
    private int postID, conditionChoice;

    private DatabaseHelper databaseHelper;



    Context context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post_activity);

        btnSaveEdit = (Button) findViewById(R.id.buttonSaveEdit);
        btnDeletePost =(Button) findViewById(R.id.buttonDetelePost);

        bookCoverImageView =(ImageView) findViewById(R.id.bookPicture1);
        titleBook = (EditText) findViewById(R.id.inputBookTitle1);
        price = (EditText) findViewById(R.id.inputPrice1);
        isbnNumber = (EditText) findViewById(R.id.inputISBN1);
        course =(EditText) findViewById(R.id.courseListEdit1) ;
        conditionSpinner = (Spinner) findViewById(R.id.spinnerCondition1);

        Intent intent = getIntent();
        selectedImagePath =intent.getExtras().getString("ImgURL");
        postID = intent.getExtras().getInt("PostID");
        userEmail=intent.getExtras().getString("UserEMAIL");

        Picasso.with(context).load(selectedImagePath).into(bookCoverImageView);
        titleBook.setText(intent.getExtras().getString("BookTitle"));
        isbnNumber.setText(intent.getExtras().getString("ISBN"));
        isbnNumber.setEnabled(false);
        titleBook.setEnabled(false);

        price.setText(String.valueOf(intent.getExtras().getDouble("Price")));
        course.setText(intent.getExtras().getString("CourseUsed"));

        databaseHelper = new DatabaseHelper(this);


        String[] conditioList ={"Select a condition","Like New","Good", "OK", "Acceptable"};
        for (int i =0; i<conditioList.length;i++) {
            if(intent.getExtras().getString("CourseUsed").equalsIgnoreCase(conditioList[i])){
                conditionChoice=i;
                break;
            }
        }
        conditionSpinner.setSelection(conditionChoice, false);

        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!isEmpty(titleBook.getText().toString())
                        && !isEmpty(isbnNumber.getText().toString())
                        && !isEmpty(price.getText().toString())
                        && !isEmpty(course.getText().toString())
                        && conditionSpinner.getSelectedItemId()!=0){
                    updatePost(postID,course.getText().toString(),Double.parseDouble(price.getText().toString())
                            ,conditionSpinner.getSelectedItem().toString());
                
                }
                else {
                    Toast.makeText(context, "Please choose the curretn condition", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost(postID);
            }
        });



    }
    public boolean isEmpty(String string){
        return string.equals("");
    }



    private void updatePost(int postNum, String newCourse, double newPrice, String newCondition ){
        long result = databaseHelper.updatePostRecord(postNum,newCourse,newPrice, newCondition);

        if(result>0){

            Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show();
            finish();

        }else {

            Toast.makeText(this, "Unable to update your record", Toast.LENGTH_SHORT).show();
        }
    }

    private void deletePost(int id){
        long result = databaseHelper.deletePost(id);
        if(result>0){
            Toast.makeText(this, "Delete Sucessfully", Toast.LENGTH_SHORT).show();
            finish();

        }else {
            Toast.makeText(this, "Unable to delete", Toast.LENGTH_SHORT).show();
        }
    }

}
