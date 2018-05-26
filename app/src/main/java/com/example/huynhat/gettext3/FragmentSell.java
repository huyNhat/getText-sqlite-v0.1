package com.example.huynhat.gettext3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.huynhat.gettext3.BarcodeScanner.CameraFace;
import com.example.huynhat.gettext3.Models.Post;
import com.example.huynhat.gettext3.SQL.DatabaseHelper;
import com.example.huynhat.gettext3.Utils.ScannerBottomDialog;
import com.example.huynhat.gettext3.Utils.SelectPhotoDialog;
import com.example.huynhat.gettext3.Utils.SessionManagement;
import com.example.huynhat.gettext3.Utils.UniversalImageLoader;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by huynhat on 2017-11-16.
 */

public class FragmentSell extends Fragment  implements SelectPhotoDialog.OnPhotoSelectedListner,
        ZXingScannerView.ResultHandler, ScannerBottomDialog.OnQRCodeScanListener {
    private Button btnPost, btnSearch;
    private ImageView bookCoverImageView, btnScanBarcode;
    private EditText titleBook, isbnNumber, price, course;
    private Spinner conditionSpinner;
    private String selectedImagePath;

    private ZXingScannerView zXingScannerView;

    //vars
    String bookSearchQuery;
    String imageURL;

    DatabaseHelper db;
    private SessionManagement sessionManagement;
    private ZXingScannerView mScannerView;

    private Bitmap selectedBitmap, thumbBitmap;
    private Uri selectedURI;

    @Override
    public void getImagePath(Uri imagePath) {
        UniversalImageLoader.setImage(imagePath.toString(),bookCoverImageView);

        selectedBitmap=null;
        selectedURI = imagePath;

    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        bookCoverImageView.setImageBitmap(bitmap);
        selectedURI=null;
        selectedBitmap=bitmap;


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sell_two, container,false);

        btnPost = (Button) view.findViewById(R.id.buttonPost);
        btnSearch =(Button) view.findViewById(R.id.buttonSearch);
        btnScanBarcode =(ImageView) view.findViewById(R.id.imageViewScanButton);


        bookCoverImageView =(ImageView) view.findViewById(R.id.bookPicture);
        titleBook = (EditText) view.findViewById(R.id.inputBookTitle);
        price = (EditText) view.findViewById(R.id.inputPrice);
        isbnNumber = (EditText) view.findViewById(R.id.inputISBN);
        course =(EditText) view.findViewById(R.id.courseListEdit) ;
        conditionSpinner = (Spinner) view.findViewById(R.id.spinnerCondition);

        db = new DatabaseHelper(getContext());
        sessionManagement = new SessionManagement(this.getActivity());

        final String userEmail = sessionManagement.getUserEmail();
        //final int userID=db.getActualID(userEmail);


        selectedImagePath = null;

        initImageLoader();

        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scanBarcode(view);


                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scanning a barcode");
                integrator.setResultDisplayDuration(0);
                //integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();



                /*
                //Testing bottom dialog scanner
                ScannerBottomDialog bottomDialog = new ScannerBottomDialog();
                bottomDialog.show(getFragmentManager(),"Bottom Dialog2");
                bottomDialog.setTargetFragment(FragmentSell.this, 1);

                */

                /*
                ScannerBottomDialog dialog = ScannerBottomDialog.newInstance(CameraFace.BACK);
                dialog.show(getActivity().getFragmentManager(), "dialogScanner");
                //dialog.setTargetFragment(getTargetFragment(FragmentSell.this), 1);
                */


                Intent intent = new Intent(getActivity(), BarcodeScanner1.class);
                startActivity(intent);



            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String scanISBN = isbnNumber.getText().toString();
                bookSearchQuery="https://www.googleapis.com/books/v1/volumes?q=isbn:"+scanISBN;
                new GetBookInfo().execute(bookSearchQuery);

                //titleBook.setVisibility(View.VISIBLE);

                if(titleBook.getText().toString().equalsIgnoreCase("NOT FOUND")
                        || scanISBN.equals("")){
                    Toast.makeText(getActivity(), "NOT FOUND or ISBN is empty", Toast.LENGTH_SHORT).show();
                }else {
                    titleBook.setVisibility(View.VISIBLE);
                    titleBook.setEnabled(false);
                    isbnNumber.setEnabled(false);
                    bookCoverImageView.setVisibility(View.VISIBLE);
                    price.setVisibility(View.VISIBLE);
                    course.setVisibility(View.VISIBLE);
                    conditionSpinner.setVisibility(View.VISIBLE);
                    btnPost.setVisibility(View.VISIBLE);

                    btnScanBarcode.setVisibility(View.INVISIBLE);
                    btnSearch.setVisibility(View.GONE);

                }

            }
        });



        bookCoverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPhotoDialog bottomDialog = new SelectPhotoDialog();
                bottomDialog.show(getFragmentManager(),"Bottom Dialog");
                bottomDialog.setTargetFragment(FragmentSell.this, 1);

            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(titleBook.getText().toString())
                        && !isEmpty(isbnNumber.getText().toString())
                        && !isEmpty(price.getText().toString())
                        && !isEmpty(course.getText().toString())
                        && conditionSpinner.getSelectedItemId()!=0){

                    Post newPost = new Post();
                    newPost.setBookTitle(titleBook.getText().toString().trim());
                    newPost.setUser_email(userEmail);
                    newPost.setImage(imageURL);
                    newPost.setPrice(Double.parseDouble(price.getText().toString()));
                    newPost.setBookTitle(titleBook.getText().toString().trim());
                    newPost.setIsbn(isbnNumber.getText().toString().trim());
                    newPost.setCourseUsed(course.getText().toString().trim());
                    newPost.setCondition(conditionSpinner.getSelectedItem().toString());

                    //Add to database
                    db.addPost(newPost);

                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                    clearAllFields();

                    titleBook.setVisibility(View.GONE);
                    bookCoverImageView.setVisibility(View.GONE);
                    price.setVisibility(View.GONE);
                    course.setVisibility(View.GONE);
                    conditionSpinner.setVisibility(View.GONE);
                    btnPost.setVisibility(View.GONE);

                    btnScanBarcode.setVisibility(View.VISIBLE);
                    btnSearch.setVisibility(View.VISIBLE);



                }
                else {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void scanBarcode(View v){
        zXingScannerView = new ZXingScannerView(getContext());
        getActivity().setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(FragmentSell.this);
        zXingScannerView.startCamera();

    }
    @Override
    public void handleResult(Result result) {
        isbnNumber.setText(result.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
           if(result.getContents()==null){
               Toast.makeText(getContext(), "Scanning was cancelled", Toast.LENGTH_SHORT).show();
           }else {
               //Toast.makeText(getContext(), "Test "+result.getContents().toString(), Toast.LENGTH_SHORT).show();
               isbnNumber.setText(result.getContents().toString());
           }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    public boolean isEmpty(String string){
        return string.equals("");
    }

    @Override
    public void onQRCodeScan(String contents) {
        if(isbnNumber != null)
        {
            isbnNumber.setText(contents);
        }
    }




    private class GetBookInfo extends AsyncTask<String,Void, String>{

        @Override
        protected String doInBackground(String... bookURLs) {
            StringBuilder bookBuilder = new StringBuilder();

            for(String bookSearchURL : bookURLs){
                //Search URLs

                HttpClient bookClient = new DefaultHttpClient();

                try{
                    //get data
                    //Create an HTTP Object
                    HttpGet bookGet = new HttpGet(bookSearchURL);
                    HttpResponse bookResponse = bookClient.execute(bookGet);
                    StatusLine bookSearchStatus = bookResponse.getStatusLine();
                    if(bookSearchStatus.getStatusCode()==200){
                        //Result here
                        HttpEntity bookEntity = bookResponse.getEntity();
                        InputStream bookContent = bookEntity.getContent();
                        InputStreamReader bookInput = new InputStreamReader(bookContent);
                        BufferedReader bookReader = new BufferedReader(bookInput);

                        String lineIn;
                        while ((lineIn=bookReader.readLine())!=null){
                            bookBuilder.append(lineIn);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }//end of forLoop

            return bookBuilder.toString();
        }//do in background

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Parse search result
            try{
                JSONObject resultObject = new JSONObject(result);
                JSONArray bookArray = resultObject.getJSONArray("items");

                //get the first book index 0
                JSONObject bookObject = bookArray.getJSONObject(0);
                JSONObject volumeObject= bookObject.getJSONObject("volumeInfo");

                try{
                    titleBook.setText(volumeObject.getString("title"));

                }catch (JSONException jse){
                    titleBook.setText("");
                    jse.printStackTrace();
                }

                try{
                    JSONObject imageInfo= volumeObject.getJSONObject("imageLinks");
                    imageURL=(imageInfo.getString("thumbnail").toString());//save link to database
                    new GetBookThumb().execute(imageInfo.getString("thumbnail"));
                }catch (JSONException jse){
                    bookCoverImageView.setImageBitmap(null);
                    jse.printStackTrace();
                }
            }catch (Exception e){
                //no result
                e.printStackTrace();
                titleBook.setText("NOT FOUND");
            }
        }
    }//end of getBookInfo

    private class GetBookThumb extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... imageURLs) {
           try{
               URL thumbURL = new URL(imageURLs[0]);

               URLConnection thumbConn = thumbURL.openConnection();
               thumbConn.connect();

               InputStream thumbIn = thumbConn.getInputStream();
               BufferedInputStream thumbBuff = new BufferedInputStream(thumbIn);
               thumbBitmap = BitmapFactory.decodeStream(thumbBuff);

               thumbBuff.close();
               thumbIn.close();
           }catch (Exception e){
               e.printStackTrace();
           }

           return "";
        }

        @Override
        protected void onPostExecute(String s) {
            bookCoverImageView.setImageBitmap(thumbBitmap);
        }
    }

    //Clear fields
    public void clearAllFields(){
        //bookCoverImageView =(ImageView) view.findViewById(R.id.bookPicture);
        titleBook.setText("");
        price.setText("");
        isbnNumber.setText("");
        course.setText("");
        conditionSpinner.setSelection(0);
        imageURL ="";


    }



}
