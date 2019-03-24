package map.android.mr_auspicious.bukarteadmin.Activities;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import map.android.mr_auspicious.bukarteadmin.Adapters.Main_Order_Adapter;
import map.android.mr_auspicious.bukarteadmin.Applications.AppController;
import map.android.mr_auspicious.bukarteadmin.Model.Main_Order;
import map.android.mr_auspicious.bukarteadmin.Model.Order;
import map.android.mr_auspicious.bukarteadmin.R;

public class My_Orders extends AppCompatActivity {


    private static final String ORDER_URL = "https://www.bukarte.com/rest_server/getallorder/API-KEY/123456";
    private static final String KEY_USER = "user_id";
    private static final String ORDER_ID = "order_id";
    private static final String PRODUCT_ID = "product_id";
    private RecyclerView recycler;
    private List<Main_Order> main_orders;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ProgressBar progressBar;
    private RelativeLayout empty_layout;
    private RelativeLayout orders_layout;
    private CoordinatorLayout cd;
    static My_Orders activityA;
    private int offset=0;
    private Main_Order_Adapter orders_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__orders);

        activityA = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        main_orders = new ArrayList<>();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progress_Bar_comp);

        empty_layout = (RelativeLayout) findViewById(R.id.empty_cart_layout);

        orders_layout = (RelativeLayout) findViewById(R.id.orders_layout);

        cd = (CoordinatorLayout) findViewById(R.id.home_screen);

        toolbar_title = (TextView) toolbar.findViewById(R.id.my_orders);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/shiv5.ttf");


        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        recycler.addOnItemTouchListener(new Main_Order_Adapter.RecyclerTouchListener(getApplicationContext(), recycler, new Main_Order_Adapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(),Order_Details.class);
                intent.putExtra("add_id",main_orders.get(position).getAddr_id());
                intent.putExtra("order_id",main_orders.get(position).getOrder_id());
                intent.putExtra("wallet_pay",main_orders.get(position).getWallet_pay());
                intent.putExtra("rest_pay",main_orders.get(position).getRest_pay());
                intent.putExtra("order_medium",main_orders.get(position).getOrder_medium());
                intent.putExtra("size",main_orders.get(position).getOrd_size());
                intent.putExtra("date",main_orders.get(position).getOrd_date());
                intent.putExtra("total_amount",main_orders.get(position).getPayable_amount());
                intent.putExtra("c_id",main_orders.get(position).getC_id());
                startActivity(intent);
            }


            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getOrdersList();
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

    void getOrdersList() {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam = new HashMap<>();
        postParam.put("offset", String.valueOf(offset));

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ORDER_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){

                                offset= response.getInt("new_offset");
                                JSONArray jsonArray = response.getJSONArray("userData");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                JSONArray jsonArray1 = jsonObject.getJSONArray("properties");
                                for (int i=0;i<jsonArray1.length();i++){
                                    JSONObject jsonObject1 =jsonArray1.getJSONObject(i);
                                    String amount = jsonObject1.getString("payable_amount");
                                    String order_id = jsonObject1.getString("order_id");
                                    String add_id = jsonObject1.getString("address_id");
                                    String date = jsonObject1.getString("order_status");
                                    String c_id = jsonObject1.getString("c_id");
                                    String wallet_pay = jsonObject1.getString("wallet_amount");
                                    String rest_pay = jsonObject1.getString("rest_pay_amount");
                                    String order_medium = jsonObject1.getString("order_medium");
                                    String order_stat = jsonObject1.getString("order_status");
                                    main_orders.add(new Main_Order(amount,"0",add_id,date,order_id,c_id,wallet_pay,rest_pay,order_medium,order_stat));
                                }

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recycler.setLayoutManager(mLayoutManager);
                                recycler.setItemAnimator(new DefaultItemAnimator());

                                orders_adapter = new Main_Order_Adapter(recycler,getApplicationContext(),main_orders);

                                recycler.setAdapter(orders_adapter);

                                orders_adapter.setOnLoadMoreListener(new Main_Order_Adapter.OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        main_orders.add(null);
                                        orders_adapter.notifyItemRemoved(main_orders.size());
                                        loadOrders();
                                    }
                                });

                                empty_layout.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                orders_layout.setVisibility(View.VISIBLE);

                            }else {
                                empty_layout.setVisibility(View.VISIBLE);
                                orders_layout.setVisibility(View.GONE);
                                cd.setBackgroundColor(getResources().getColor(R.color.white));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(My_Orders.this, e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(My_Orders.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Passing some request headers
             */
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

    private void loadOrders() {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam = new HashMap<>();
        postParam.put("offset", String.valueOf(offset));

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ORDER_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){

                                main_orders.remove(main_orders.size() - 1);
                                orders_adapter.notifyItemRemoved(main_orders.size());

                                offset = response.getInt("new_offset");

                                JSONArray jsonArray = response.getJSONArray("userData");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                JSONArray jsonArray1 = jsonObject.getJSONArray("properties");
                                for (int i=0;i<jsonArray1.length();i++){
                                    JSONObject jsonObject1 =jsonArray.getJSONObject(i);
                                    String amount = jsonObject1.getString("payable_amount");
                                    String order_id = jsonObject1.getString("order_id");
                                    String size = jsonObject1.getString("item_count");
                                    String add_id = jsonObject1.getString("address_id");
                                    String date = jsonObject1.getString("order_status");
                                    String c_id = jsonObject1.getString("c_id");
                                    String wallet_pay = jsonObject1.getString("wallet_pay");
                                    String rest_pay = jsonObject1.getString("rest_pay");
                                    String order_medium = jsonObject1.getString("order_medium");
                                    String order_stat = jsonObject1.getString("status");
                                    main_orders.add(new Main_Order(amount,size,add_id,date,order_id,c_id,wallet_pay,rest_pay,order_medium,order_stat));
                                }

                                orders_adapter.setLoaded();
                                orders_adapter.notifyDataSetChanged();

                            }else {
                                empty_layout.setVisibility(View.VISIBLE);
                                orders_layout.setVisibility(View.GONE);
                                cd.setBackgroundColor(getResources().getColor(R.color.white));
                            }

                        } catch (JSONException e) {
                            Toast.makeText(My_Orders.this, e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(My_Orders.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Passing some request headers
             */
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
