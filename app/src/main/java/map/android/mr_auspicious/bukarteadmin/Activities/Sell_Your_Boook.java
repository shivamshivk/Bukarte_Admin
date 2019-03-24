package map.android.mr_auspicious.bukarteadmin.Activities;


import map.android.mr_auspicious.bukarteadmin.R;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import map.android.mr_auspicious.bukarteadmin.Applications.AppController;
import map.android.mr_auspicious.bukarteadmin.Model.Categories;
import map.android.mr_auspicious.bukarteadmin.Sessions.Seller_Session;
import map.android.mr_auspicious.bukarteadmin.Tokenizer.Space_Tokenizer;


public class Sell_Your_Boook extends AppCompatActivity {

    private static final String BOOK_URL = "https://www.bukarte.com/rest_server/mainSellerInsertBooks/API-KEY/123456";
    private String KEY_CATEGORY = "book_category";
    private String KEY_BOOK_NAME = "book_name";
    private String KEY_AUTHOR_NAME = "author_name";
    private String KEY_SUBJECT = "subject_id";
    private String KEY_CLASS = "book_class_id";
    private String KEY_SCHOOL = "school_id";
    private String KEY_COLLEGE = "college_id";
    private String KEY_LOCATION = "location_id";
    private String KEY_PIN = "pin_id";
    private String KEY_EDITION = "edition_year";
    private String KEY_DESCRIPTION = "description";
    private String KEY_PURCHASE_YEAR = "purchase_year";
    private String KEY_BOOK_CONDITION = "book_condition";
    private String KEY_MRP = "printed_mrp";
    private String KEY_SELLING_PRICE = "selling_price";
    private String KEY_SELLER_ID = "sellerid";
    private ProgressDialog mProgressDialog;
    private RelativeLayout relativeLayout;
    private ImageView close;
    static Sell_Your_Boook activityA;
    private List<Categories> categories= new ArrayList<>();
    private ProgressBar p_bar;
    private Spinner spinner_cat;
    private EditText editText;
    private EditText authorEdit;
    private Button button;
    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private EditText desc;
    private LinearLayout linearLayout;
    private EditText book_mrp;
    private EditText book_price;
    private AutoCompleteTextView book_Purchase_Edit;
    private AutoCompleteTextView book_Edition_Edit;
    private AutoCompleteTextView book_Subject;
    private List<HashMap<String,String>> subject_lists;
    private HashMap<String,String> subject_Details;
    private List<String> bcLists;
    private String category;
    private static final String CATEGORIES_URL = "https://www.bukarte.com/developers/example_api/categoryavailability/API-KEY/123456";
    private static final String bc_url = "https://www.bukarte.com/developers/example_api/bookconditionavailability/API-KEY/123456";
    private static final String subject_url= "https://www.bukarte.com/developers/example_api/subjectAvailability/API-KEY/123456";
    private static final String IMAGE_URL = "https://www.bukarte.com/rest_server/bookimage/API-KEY/123456";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Uri image;
    private String book_id;
    private static final int CAMERA_PIC_REQUEST =100 ;
    private static final int GALLERY_PIC_REQUEST = 1999;
    private static final String TAG = Sell_Your_Boook.class.getSimpleName();
    private Button upload_button;
    private Seller_Session seller_session;
    private Toolbar toolbar;
    private HashMap<String,String> categories_p=new HashMap<>();
    private List<String> cats=new ArrayList<>();
    private Button btn_post;
    private String book_id_s="";
    @SuppressLint("InlinedApi")
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    //permission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission =  ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        @SuppressLint("InlinedApi") int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED && cameraPermission!= PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell__your__boook);

        linearLayout = (LinearLayout) findViewById(R.id.relative_layout);
        p_bar = (ProgressBar) findViewById(R.id.p_bar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activityA=this;
        verifyStoragePermissions(this);

        seller_session = new Seller_Session(getApplicationContext());


        multiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.book_condition);
        multiAutoCompleteTextView.setTokenizer(new Space_Tokenizer());

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/shiv.ttf");

        spinner_cat=(Spinner) findViewById(R.id.spinner_cat);

        authorEdit = (EditText) findViewById(R.id.book_Author);
        ((TextInputLayout) findViewById(R.id.book_author_layout)).setTypeface(tf);
        authorEdit.setTypeface(tf);

        authorEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        editText = (EditText) findViewById(R.id.book_name);
        editText.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.book_name_layout)).setTypeface(tf);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;

            }
        });



        multiAutoCompleteTextView.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.book_condition_layout)).setTypeface(tf);

        multiAutoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent paramMotionEvent) {
                // TODO Auto-generated method stub
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                multiAutoCompleteTextView.showDropDown();
                return false;
            }
        });

        button = (Button) findViewById(R.id.upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = "Open Photo";
                CharSequence[] itemlist ={"Take a Photo",
                        "Pick from Gallery",
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Sell_Your_Boook.this);
                //builder.setIcon(R.drawable.icon_app);
                builder.setTitle(title);
                builder.setItems(itemlist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Take Photo
                                cameraIntent();
                                break;
                            case 1:// Choose Existing Photo
                                galleryIntent();
                                break;

                            default:
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();
            }
        });

        book_Purchase_Edit = (AutoCompleteTextView) findViewById(R.id.book_purchase_edit);
        book_Purchase_Edit.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.py_layout)).setTypeface(tf);


        book_Purchase_Edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;

            }
        });

        book_Edition_Edit = (AutoCompleteTextView) findViewById(R.id.book_edition_edit);
        book_Edition_Edit.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.edition_layout)).setTypeface(tf);

        book_Edition_Edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                hideSoftKeyboard();
                return false;
            }
        });

        book_Edition_Edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;

            }
        });

        int inputType = book_Purchase_Edit.getInputType();
        book_Purchase_Edit.setRawInputType(inputType & ~EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE);


        book_mrp = (EditText) findViewById(R.id.book_mrp_edit);

        book_mrp.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.mrp_layout)).setTypeface(tf);


        book_mrp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;

            }
        });

        book_price = (EditText) findViewById(R.id.your_price_edit);

        book_price.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.yp_layout)).setTypeface(tf);


        book_price.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });


        desc = (EditText) findViewById(R.id.desc);

        desc.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.desc_layout)).setTypeface(tf);


        desc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        new GetCategories().execute(CATEGORIES_URL);
        new GetBcs().execute(bc_url);

        btn_post = (Button) findViewById(R.id.post);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().isEmpty() && !authorEdit.getText().toString().trim().isEmpty() && !multiAutoCompleteTextView.getText().toString().isEmpty() && !book_mrp.getText().toString().isEmpty() && !book_price.getText().toString().isEmpty()){
                    showProgressDialog();
                    uploadData();
                }else {
                    Toast.makeText(Sell_Your_Boook.this, "Please fill it out", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    private class GetCategories extends AsyncTask<String,Void,String> {


        HttpURLConnection urlConnection;
        String json;

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "n");
                }

                json = sb.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return json;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            JSONArray locations;
            try {

                JSONObject jsonObj = new JSONObject(s);

                categories.clear();

                locations = jsonObj.getJSONArray("userData");
                for (int i = 0; i < locations.length(); i++) {

                    JSONObject jsonObject = locations.getJSONObject(i);
                    String category_name = jsonObject.getString("category_name");
                    String category_id = jsonObject.getString("category_id");
                    categories.add(new Categories(category_name,category_id));
                    categories_p.put(category_name,category_id);
                    cats.add(category_name);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Sell_Your_Boook.this,
                        android.R.layout.simple_spinner_dropdown_item,cats);
                spinner_cat.setAdapter(adapter);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


        }

    }


    private class GetBcs extends AsyncTask<String,Void,String> {


        HttpURLConnection urlConnection;
        String json;

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "n");
                }

                json = sb.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return json;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            JSONArray locations;
            try {

                JSONObject jsonObj = new JSONObject(s);

                bcLists = new ArrayList<>();

                locations = jsonObj.getJSONArray("userData");
                for (int i = 0; i < locations.length(); i++) {

                    JSONObject jsonObject = locations.getJSONObject(i);
                    String location_name = jsonObject.getString("condition_type");
                    bcLists.add(location_name);

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Sell_Your_Boook.this,
                        android.R.layout.simple_list_item_1,bcLists );
                multiAutoCompleteTextView.setAdapter(adapter);

                p_bar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);


            } catch (JSONException e1) {
                e1.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


        }

    }


    private class GetSubjects extends AsyncTask<String,Void,String> {


        HttpURLConnection urlConnection;
        String json;

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "n");
                }

                json = sb.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return json;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            subject_lists = new ArrayList<>();


            try {
                JSONObject jsonObj = new JSONObject(json);
                subject_Details = new HashMap<>();
                ArrayList<String> subjectNames = new ArrayList<>();
                JSONArray locations = jsonObj.getJSONArray("userData");

                for (int i = 0; i < locations.length(); i++) {

                    JSONObject jsonObject = locations.getJSONObject(i);
                    String location_name = jsonObject.getString("subject_name");
                    String location_id = jsonObject.getString("subject_id");

                    subjectNames.add(location_name);

                    subject_Details.put(location_name,location_id);

                    subject_lists.add(subject_Details);

                }

                ArrayAdapter<String> subject_Adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_list,subjectNames);
                book_Subject.setAdapter(subject_Adapter);

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch(NullPointerException e){
                e.getStackTrace();
            }


        }

    }


    private void galleryIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_PIC_REQUEST);

    }

    private void cameraIntent() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

    }


    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        ImageView image_view = (ImageView) findViewById(R.id.book_pic);


        if (reqCode == GALLERY_PIC_REQUEST &&  resultCode == RESULT_OK) {

            image = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(image,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            image_view.setVisibility(View.VISIBLE);
            btn_post.setVisibility(View.VISIBLE);
            image_view.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Toast.makeText(activityA, "Image Uploaded , scroll to continue", Toast.LENGTH_SHORT).show();

        }else if(reqCode == CAMERA_PIC_REQUEST &&  resultCode == RESULT_OK){

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image= getImageUri(getApplicationContext(),photo);
            image_view.setVisibility(View.VISIBLE);
            btn_post.setVisibility(View.VISIBLE);
            image_view.setImageBitmap(photo);
            Toast.makeText(activityA, "Image Uploaded , scroll to continue", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onBackPressed() {

        finish();

    }

    public void uploadMultipart() {
        //getting the actual path of the image
        String path = getPath(image);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, IMAGE_URL)
                    .addFileToUpload(path, "book_photo") //Adding file
                    .addParameter("book_id", book_id_s) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadData() {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";


        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_CATEGORY, categories_p.get(spinner_cat.getSelectedItem().toString()));
        postParam.put(KEY_BOOK_NAME,editText.getText().toString().trim());
        postParam.put(KEY_AUTHOR_NAME, authorEdit.getText().toString().trim());
        postParam.put(KEY_EDITION, book_Edition_Edit.getText().toString());
        postParam.put(KEY_DESCRIPTION, desc.getText().toString().trim());
        postParam.put(KEY_PURCHASE_YEAR, book_Purchase_Edit.getText().toString().trim());
        postParam.put(KEY_BOOK_CONDITION, multiAutoCompleteTextView.getText().toString().trim());
        postParam.put(KEY_MRP, book_mrp.getText().toString().trim());
        postParam.put(KEY_SELLING_PRICE, book_price.getText().toString());
        postParam.put(KEY_DESCRIPTION, desc.getText().toString().trim());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                BOOK_URL, new JSONObject(postParam),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            hideProgressDialog();

                            if(status.equals("200")) {
                                finish();
                                Toast.makeText(Sell_Your_Boook.this, "Boook Uploaded", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(Sell_Your_Boook.this,"Connection error",Toast.LENGTH_LONG).show();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }




}