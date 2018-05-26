package com.example.huynhat.gettext3.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.huynhat.gettext3.HomeActivity;
import com.example.huynhat.gettext3.Models.Post;
import com.example.huynhat.gettext3.Models.User;
import com.example.huynhat.gettext3.R;
import com.example.huynhat.gettext3.SQL.DatabaseHelper;
import com.example.huynhat.gettext3.Utils.InputValidation;
import com.example.huynhat.gettext3.Utils.SessionManagement;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LoginActivity.this;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;

    private AppCompatButton btnLogin;

    private AppCompatTextView registerLink;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private SessionManagement sessionManagement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hideProgressBar();
        initViews();
        initListner();
        initObjects();


        //SAMPLE DATA--- COMMENT OUT AFTER FIRST RUN
        //sampleData();//Uncomment for some sample data: such users or ads -- Comment out before login or register

        //Should be in the Splash Screen
        if(sessionManagement.isLoggedIn()){
            Intent intent = new Intent(activity,HomeActivity.class);
            startActivity(intent);
        }
        /*
        else {
            if(!sessionManagement.datbaseLoad()){
                sampleData();
            }
        }
        */
    }

    //Initialize views
    private void initViews(){
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        inputEmail = (TextInputEditText) findViewById(R.id.textInputEditEmail);
        inputPassword = (TextInputEditText) findViewById(R.id.textInputEditPassword);

        btnLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        registerLink = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
    }

    //Initialize Button Lisnterner
    private void initListner(){
        btnLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

    }

    //Initialize objects to be used

    private void initObjects(){
        databaseHelper = new DatabaseHelper(activity);
        inputValidation= new InputValidation(activity);
        sessionManagement = new SessionManagement(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;

            case R.id.textViewLinkRegister:
                //Navigate to RegisterActivity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(inputEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(inputEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        //If success sent to Home & Store user in the preferences:
        if (databaseHelper.checkUser(inputEmail.getText().toString().trim(),
                inputPassword.getText().toString().trim())){

            //Saving user loggin sessin the preference
            sessionManagement.createLoginSession(inputEmail.getText().toString());


            //Going to the Home Activity
            Intent intent = new Intent(activity, HomeActivity.class);
            emptyInputEditText();



            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        inputEmail.setText(null);
        inputPassword.setText(null);
    }

    //Hiding progress bar

    private void hideProgressBar(){
        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Custom Toast
     * @param
     */
    public void myToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public void sampleData(){
        User newUser = new User();
        newUser.setName("Huy");
        newUser.setEmail("huy@nhat.ca");
        newUser.setPassword("huy");
        databaseHelper.addUser(newUser);

        User newUser1 = new User();
        newUser1.setName("Nhat");
        newUser1.setEmail("nhat@nhat.ca");
        newUser1.setPassword("nhat");
        databaseHelper.addUser(newUser1);

        Post newPost = new Post();
        newPost.setUser_email("nhat@nhat.ca");
        newPost.setBookTitle("Android Boot Camp for Developers Using Java: A Guide to Creating Your First Android Apps");
        newPost.setImage("http://books.google.com/books/content?id=msuFCwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
        newPost.setIsbn("9781337027670");
        newPost.setPrice(75);
        newPost.setCondition("Like New");
        newPost.setCourseUsed("CSIS3175");
        databaseHelper.addPost(newPost);

        Post newPost1 = new Post();
        newPost1.setUser_email("nhat@nhat.ca");
        newPost1.setBookTitle("Psychology AS");
        newPost1.setImage("http://books.google.com/books/content?id=1giK8vyEz_cC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
        newPost1.setIsbn("0748794638");
        newPost1.setPrice(25);
        newPost1.setCondition("Like New");
        newPost1.setCourseUsed("PSYC1200");
        databaseHelper.addPost(newPost1);

        Post newPost2 = new Post();
        newPost2.setUser_email("nhat@nhat.ca");
        newPost2.setBookTitle("Diversity and Evolutionary Biology of Tropical Flowers");
        newPost2.setImage("http://books.google.com/books/content?id=8_DfMSS9r9cC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
        newPost2.setIsbn("0521565103");
        newPost2.setPrice(125);
        newPost2.setCondition("Like New");
        newPost2.setCourseUsed("BIOL1100");
        databaseHelper.addPost(newPost2);

        Post newPost3 = new Post();
        newPost3.setUser_email("huy@nhat.ca");
        newPost3.setBookTitle("Introduction To Algorithms");
        newPost3.setImage("http://books.google.com/books/content?id=NLngYyWFl_YC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
        newPost3.setIsbn("0262032937");
        newPost3.setPrice(100);
        newPost3.setCondition("Like New");
        newPost3.setCourseUsed("CSIS4175");
        databaseHelper.addPost(newPost3);
    }


}
