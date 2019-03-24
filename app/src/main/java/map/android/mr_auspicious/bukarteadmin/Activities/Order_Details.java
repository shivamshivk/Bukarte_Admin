package map.android.mr_auspicious.bukarteadmin.Activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import map.android.mr_auspicious.bukarteadmin.Adapters.Orders_Details_Adapter;
import map.android.mr_auspicious.bukarteadmin.Applications.AppController;
import map.android.mr_auspicious.bukarteadmin.Model.Order;
import map.android.mr_auspicious.bukarteadmin.R;


public class Order_Details extends AppCompatActivity {


    private TextView status;
    private Button confirm_btn;
    private Button cancel_btn;
    private Button call_btn;
    private Button email_btn;
    private Button notify_btn;
    private static final String TRACKING_URL ="https://www.bukarte.com/rest_server/changeTrackingStatus/API-KEY/123456";
    private static final String STATUS_URL ="https://www.bukarte.com/rest_server/updateOrderStatus/API-KEY/123456";
    private static final String ORDER_DETAILS = "https://www.bukarte.com/rest_server/getOrderDetails/API-KEY/123456";
    private static final String CS_DETAILS_URL = "https://www.bukarte.com/rest_server/customerDetails/API-KEY/123456";
    private static final String ADDRESS_URL = "https://www.bukarte.com/rest_server/customerDeliveryAddressdetails/API-KEY/123456";
    private RecyclerView recyclerView;
    private TextView date;
    private TextView order_id;
    private TextView total_amount;
    private String add_id;
    private String name_s;
    private String phone_s;
    private String address_s;
    private String address_2_s;
    private String landmark_s;
    private String pin_s;
    private String city_s;
    private String state_s;
    private TextView name;
    private TextView phone_no;
    private TextView adrress;
    private TextView address2;
    private TextView landmark;
    private TextView pin;
    private TextView city;
    private TextView state;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private List<Order> products = new ArrayList<>();
    private String order_id_s;
    private String date_s;
    private String wallet_pay;
    private String rest_pay;
    private String order_medium;
    private String size;
    private String order_status_s;
    private String total_amount_s;
    private Orders_Details_Adapter orders_adapter;
    private LinearLayout lin;
    private ProgressBar progressBar;
    private LinearLayout address_layout;
    private ProgressDialog mProgressDialog;
    private String waybill;
    private int id = 0;
    private int status_id = 0;
    private RadioButton order_confirm;
    private RadioButton in_trasnit;
    private RadioButton order_shipped;
    private RadioButton order_delivered;
    private TextView wallet_pay_amount;
    private TextView rest_pay_amount;
    private TextView order_medium_t;
    private RelativeLayout wallet_layout;
    private RelativeLayout rest_pay_layout;
    private String mobile;
    private String c_name;
    private String email;
    private TextView c_name_text;
    private TextView mobile_text;
    private TextView email_text;
    private int call_pos = 0;
    private String av_warehouse="";
    private String prep_delivery="";
    private String shipp_status="";
    private String deliv_status="";
    private String tracking_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            add_id = extras.getString("add_id");
            order_id_s = extras.getString("order_id");
            wallet_pay = extras.getString("wallet_pay");
            rest_pay = extras.getString("rest_pay");
            order_medium = extras.getString("order_medium");
            size = extras.getString("size");
            date_s = extras.getString("date");
            order_status_s = extras.getString("order_status");
            total_amount_s = extras.getString("total_amount");
            waybill = extras.getString("c_id");
        }


        status = findViewById(R.id.status);


        confirm_btn = findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                orderStatus("1");
            }
        });

        cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                orderStatus("0");
            }
        });

        call_btn = findViewById(R.id.call_btn);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermissionGranted()){
                    call_action(mobile);
                }
            }
        });

        email_btn = findViewById(R.id.email_btn);
        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "order@bukarte.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding your Order ID - "+order_id_s);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi "+c_name + ". Your Order has been  ");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        notify_btn = findViewById(R.id.notify_btn);
        notify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Order_Details.this, "Notifying..", Toast.LENGTH_SHORT).show();
            }
        });

        order_confirm = findViewById(R.id.order_confirm);
        order_confirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (order_confirm.isChecked()){
                    if (order_status_s.equals("0") || order_status_s.equals("1")){
                        trackOrderStatus("1","0");
                    }else {
                        Toast.makeText(Order_Details.this, "Bhaiya Order Pending h , Ghanta Update Hoga !!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    if ( order_status_s.equals("1")){
                        trackOrderStatus("1","1");
                    }else {
                        Toast.makeText(Order_Details.this, "Areh Pahle Order Confirm kijiye na ji", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



        in_trasnit = findViewById(R.id.in_transit);
        in_trasnit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (order_confirm.isChecked()){
                    if (order_status_s.equals("0") || order_status_s.equals("1")){
                        trackOrderStatus("2","0");
                    }else {
                        Toast.makeText(Order_Details.this, "Bhaiya Order Pending h , Ghanta Update Hoga !!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    if ( order_status_s.equals("1")){
                        trackOrderStatus("2","1");
                    }else {
                        Toast.makeText(Order_Details.this, "Areh Pahle Order Confirm kijiye na ji", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        order_shipped = findViewById(R.id.order_shipped);
        order_shipped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (order_confirm.isChecked()){
                    if (order_status_s.equals("0") || order_status_s.equals("1")){
                        trackOrderStatus("3","0");
                    }else {
                        Toast.makeText(Order_Details.this, "Bhaiya Order Pending h , Ghanta Update Hoga !!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    if ( order_status_s.equals("1")){
                        trackOrderStatus("3","1");
                    }else {
                        Toast.makeText(Order_Details.this, "Areh Pahle Order Confirm kijiye na ji", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        order_delivered = findViewById(R.id.order_delivered);
        order_delivered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (order_confirm.isChecked()){
                    if (order_status_s.equals("0") || order_status_s.equals("1")){
                        trackOrderStatus("4","0");
                    }else {
                        Toast.makeText(Order_Details.this, "Bhaiya Order Pending h , Ghanta Update Hoga !!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    if ( order_status_s.equals("1")){
                        trackOrderStatus("4","1");
                    }else {
                        Toast.makeText(Order_Details.this, "Areh Pahle Order Confirm kijiye na ji", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        c_name_text = findViewById(R.id.cs_name);
        mobile_text = findViewById(R.id.cs_phone_no);
        email_text = findViewById(R.id.cs_email);

        wallet_pay_amount = findViewById(R.id.wallet_pay_amount);
        rest_pay_amount = findViewById(R.id.rest_pay_amount);
        order_medium_t = findViewById(R.id.order_medium);
        wallet_layout = findViewById(R.id.wallet_layout);
        rest_pay_layout = findViewById(R.id.rest_pay_layout);

        if (wallet_pay.equals("0")) {
            wallet_layout.setVisibility(View.GONE);
            rest_pay_layout.setVisibility(View.GONE);
        } else {
            wallet_pay_amount.setText(wallet_pay + " INR");
            rest_pay_amount.setText(rest_pay + " INR");
        }

        order_medium_t.setText(order_medium);


        if (order_status_s.equals("0")) {
            status.setText("Order Canelled");
        } else if (order_status_s.equals("1")) {
            status.setText("Order Confirmed");
        } else if (order_status_s.equals("2")) {
            status.setText("Order Pending !");
        }



        address_layout = findViewById(R.id.address_layout);

        toolbar_title = (TextView) findViewById(R.id.my_product);
        toolbar_title.setText("Order ID - " + order_id_s);

        lin = (LinearLayout) findViewById(R.id.linear_layout);
        progressBar = (ProgressBar) findViewById(R.id.p_bar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_v);

        order_id = (TextView) findViewById(R.id.order_id);
        order_id.setText(order_id_s);
        date = (TextView) findViewById(R.id.order_date);
        date.setText(date_s);

        total_amount = (TextView) findViewById(R.id.total_amount);
        total_amount.setText(total_amount_s);

        recyclerView.setNestedScrollingEnabled(false);

        getCurrentTrackingStatus();
//        if (order_status_s.equals("1")){
//            order_status.setText("In Transit");
//            order_status.setTextColor(getResources().getColor(R.color.prep));
//        }else if (order_status_s.equals("2") ){
//            order_status.setText("Shipped");
//            order_status.setTextColor(getResources().getColor(R.color.shipp));
//        }else if (order_status_s.equals("3")){
//            order_status.setText("Delivered");
//            order_status.setTextColor(getResources().getColor(R.color.deliv));
//        }else {
//            order_status.setText("Order Confirmed");
//            order_status.setTextColor(getResources().getColor(R.color.prep));
//        }


        name = (TextView) findViewById(R.id.name);
        phone_no = (TextView) findViewById(R.id.phone_no);
        adrress = (TextView) findViewById(R.id.address);
        address2 = (TextView) findViewById(R.id.address_2);
        landmark = (TextView) findViewById(R.id.landmark);
        pin = (TextView) findViewById(R.id.pin);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);

        getAddress();
    }

    private void getAddress() {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";


        Map<String, String> postParam = new HashMap<>();
        postParam.put("address_id", add_id);


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ADDRESS_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");
                            if (status.equals("200")) {
                                JSONArray jsonArray = response.getJSONArray("address_data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject properties = jsonArray.getJSONObject(i);

                                    name_s = properties.getString("c_name");
                                    phone_s = properties.getString("mobile");
                                    address_s = properties.getString("add_l1");
                                    address_2_s = properties.getString("add_l2");
                                    landmark_s = properties.getString("landmark");
                                    pin_s = properties.getString("postal_code");
                                    city_s = properties.getString("city");
                                    state_s = properties.getString("state");
                                }

                                name.setText(name_s);
                                phone_no.setText(phone_s);
                                adrress.setText(address_s);
                                address2.setText(address_2_s);
                                landmark.setText(landmark_s);
                                pin.setText(pin_s);
                                city.setText(city_s);
                                state.setText(state_s);

                                getCSDetails();

                            } else {
                                id = 1;
                                progressBar.setVisibility(View.GONE);
                                lin.setVisibility(View.VISIBLE);
                                address_layout.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Order_Details.this, "Connection Error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                address_layout.setVisibility(View.GONE);
                lin.setVisibility(View.GONE);
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


//
//    void cancelProduct(int i,String can_type){
//
//        // Tag used to cancel the request
//        String tag_json_obj = "json_obj_req";
//
//
//        Map<String, String> postParam= new HashMap<>();
//        postParam.put("order_id",order_id_s);
//        postParam.put("product_id",products.get(i).getId());
//        postParam.put("can_type",can_type);
//
//        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                CANCEL_URL_, new JSONObject(postParam),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        hideProgressDialog();
//                        try {
//                            String status = response.getString("status");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                hideProgressDialog();
//                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG);
//            }
//        }) {
//
//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//
//        };
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void showDialog() {
    }

    private void getCSDetails() {

// Tag used to cancel the request
        String tag_json_obj = "json_obj_req";


        Map<String, String> postParam = new HashMap<>();
        postParam.put("c_id", waybill);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                CS_DETAILS_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        try {

                            String status = response.getString("status");
                            if (status.equals("200")) {
                                c_name = response.getString("c_name");
                                email = response.getString("email");
                                mobile = response.getString("mobile");

                                c_name_text.setText(c_name);
                                mobile_text.setText(mobile);
                                email_text.setText(email);

                                getOrderDetails();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
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

    void getOrderDetails() {


        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";


        Map<String, String> postParam = new HashMap<>();
        postParam.put("order_id", order_id_s);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ORDER_DETAILS, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        try {

                            String status = response.getString("status");
                            if (status.equals("200")) {
                                JSONArray jsonArray1 = response.getJSONArray("userData");

                                for (int k = 0; k < jsonArray1.length(); k++) {
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(k);
                                    JSONObject jsonObject3 = jsonObject2.getJSONObject("options");
                                    String id = jsonObject2.getString("id");
                                    String product_name = jsonObject2.getString("name");
                                    String img_url = jsonObject3.getString("product_image");
                                    String price = jsonObject2.getString("price");
                                    String qty = jsonObject2.getString("qty");
                                    products.add(new Order(id, img_url, product_name, price, qty));
                                }

                                JSONArray jsonArray = response.getJSONArray("track_details");
                                for (int i=0;i<1;i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    tracking_id = jsonObject.getString("tracking_id");
                                    av_warehouse = jsonObject.getString("item_ava_warehouse");
                                    prep_delivery = jsonObject.getString("item_prepared_delivery");
                                    shipp_status = jsonObject.getString("item_shipped_status");
                                    deliv_status = jsonObject.getString("item_delivery_status");
                                }


                                orders_adapter = new Orders_Details_Adapter(getApplicationContext(), products, new Orders_Details_Adapter.Cancel_Listener() {
                                    @Override
                                    public void cancel_click(View view, int position, String can_type) {

                                    }

                                    @Override
                                    public void track_Click(View view, int position, int id) {
                                        Intent intent = new Intent(getApplicationContext(), Web_View_Activity.class);
                                        intent.putExtra("tracking_url", "https://track.aftership.com/india-post/" + "EQ973550842IN?");
                                        startActivity(intent);
                                    }
                                });

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(orders_adapter);

                                id = 0;
                                progressBar.setVisibility(View.GONE);
                                lin.setVisibility(View.VISIBLE);
                                address_layout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
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

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                requestPermissions( new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action(mobile);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @SuppressLint("MissingPermission")
    public void call_action(String pos) {
        String phnum = pos;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));
        startActivity(callIntent);
    }

    
    void trackOrderStatus(final String status_id , final String tracking_id){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";


        Map<String, String> postParam = new HashMap<>();
        postParam.put("order_id", order_id_s);
        postParam.put("status_id",status_id);
        postParam.put("tracking_id",tracking_id);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                TRACKING_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        try {

                            hideProgressDialog();
                            String stat = response.getString("status");
                            if (stat.equals("200")) {

                                if (status_id.equals("1") && tracking_id.equals("0")){
                                    av_warehouse = "0";
                                }else {
                                    av_warehouse = "1";
                                }

                                if (status_id.equals("2") && tracking_id.equals("0")){
                                    prep_delivery = "0";
                                }else {
                                    prep_delivery = "1";
                                }

                                if (status_id.equals("3") && tracking_id.equals("0")){
                                    shipp_status = "0";
                                }else {
                                    shipp_status = "1";
                                }

                                if (status_id.equals("4") && tracking_id.equals("0")){
                                    deliv_status = "0";
                                }else {
                                    deliv_status = "1";
                                }

                                getCurrentTrackingStatus();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @SuppressLint("ShowToast")
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
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
    
    void orderStatus(final String status_id){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";


        Map<String, String> postParam = new HashMap<>();
        postParam.put("order_id", order_id_s);
        postParam.put("status_id",status_id);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                STATUS_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        try {

                            hideProgressDialog();
                            String stat = response.getString("status");
                            if (stat.equals("200")) {
                                if (status_id.equals("0")){
                                    order_status_s = "0";
                                    status.setText("Order Cancelled !!");
                                }else{
                                    order_status_s = "1";
                                    status.setText("Order Confirmed Yo");
                                }
                                Toast.makeText(Order_Details.this, "Order Updated", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
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

    void getCurrentTrackingStatus(){
        if (av_warehouse.equals("1") && prep_delivery.equals("1") && shipp_status.equals("1") && deliv_status.equals("1")){
            order_delivered.setChecked(true);
            order_shipped.setChecked(true);
            order_confirm.setChecked(true);
            in_trasnit.setChecked(true);
        }else if (av_warehouse.equals("1") && prep_delivery.equals("1") && shipp_status.equals("1")){
            order_shipped.setChecked(true);
            order_confirm.setChecked(true);
            in_trasnit.setChecked(true);
        }else if (av_warehouse.equals("1") && prep_delivery.equals("1")){
            in_trasnit.setChecked(true);
            order_confirm.setChecked(true);
        }else if (av_warehouse.equals("1") ){
            order_confirm.setChecked(true);
        }else {
            order_delivered.setChecked(false);
            order_shipped.setChecked(false);
            order_confirm.setChecked(false);
            in_trasnit.setChecked(false);
        }
    }


}
