package map.android.mr_auspicious.bukarteadmin.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
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
import map.android.mr_auspicious.bukarteadmin.Adapters.List_Adapter;
import map.android.mr_auspicious.bukarteadmin.Adapters.Request_Adapter;
import map.android.mr_auspicious.bukarteadmin.Applications.AppController;
import map.android.mr_auspicious.bukarteadmin.R;
import map.android.mr_auspicious.bukarteadmin.Model.Request_;

/**
 * A simple {@link Fragment} subclass.
 */
public class Requested_Books extends Fragment {


    private static final String SEARCH_URL = "https://developers.bukarte.com/example_api/allBookrequest/API-KEY/123456";
    private SwipeRefreshLayout refreshLayout;
    private View rootview;
    private ProgressBar p_bar;
    private RecyclerView recyclerView;
    private List<Request_> requests = new ArrayList<>();
    private List_Adapter mAdpater;
    private String b_name;
    private String a_name;
    private String p_name;
    private String c_id;
    private String date_time;
    private int call_pos;
    private int offset=0;

    public Requested_Books() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_requested__books, container, false);

        refreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.refresh_);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                p_bar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                offset=0;
                getRequestedBooks();
            }
        });

        p_bar = (ProgressBar) rootview.findViewById(R.id.p_bar);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view);

        // Inflate the layout for this fragment
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getRequestedBooks();
    }

    void loadRequestBooks(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        HashMap<String,String> postParam = new HashMap<>();
        postParam.put("offset", String.valueOf(offset));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                SEARCH_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            requests.remove(requests.size() - 1);
                            mAdpater.notifyItemRemoved(requests.size());


                            String status = response.getString("status");
                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("userData");

                                offset = response.getInt("new_offset");

                                for(int i =0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    b_name = jsonObject.getString("book_name");
                                    a_name = jsonObject.getString("author_name");
                                    p_name = jsonObject.getString("publisher");
                                    c_id = jsonObject.getString("c_id");
                                    date_time = jsonObject.getString("requested_time");
                                    String mobile = jsonObject.getString("mobile_number");

                                    requests.add(new Request_(b_name,c_id,p_name,a_name,date_time,mobile));
                                }


                                mAdpater.setLoaded();
                                mAdpater.notifyDataSetChanged();

                              }else {
                                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                                p_bar.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                p_bar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
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

    void getRequestedBooks(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        HashMap<String,String> postParam = new HashMap<>();
        postParam.put("offset", String.valueOf(offset));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                SEARCH_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("userData");

                                offset = response.getInt("new_offset");

                                for(int i =0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    b_name = jsonObject.getString("book_name");
                                    a_name = jsonObject.getString("author_name");
                                    p_name = jsonObject.getString("publisher");
                                    c_id = jsonObject.getString("c_id");
                                    date_time = jsonObject.getString("requested_time");
                                    String mobile = jsonObject.getString("mobile_number");

                                    requests.add(new Request_(b_name,c_id,p_name,a_name,date_time,mobile));
                                }

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());


                                mAdpater = new List_Adapter(recyclerView,getActivity(), requests, new List_Adapter.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {

                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }

                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onShareClick(View view, int position) {
                                        Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
                                        intent2.setType("text/plain");
                                        intent2.putExtra(Intent.EXTRA_TEXT,"Book Name = "+requests.get(position).getB_name() + System.lineSeparator() + "Author Name = "+requests.get(position).getA_name() + System.lineSeparator() + "Publisher Name = "+requests.get(position).getP_name()+System.lineSeparator() + "Requested At = "+requests.get(position).getRequest_time()+System.lineSeparator()+ "C_ID = "+requests.get(position).getC_id());
                                        startActivity(Intent.createChooser(intent2, "Share via"));
                                    }

                                    @Override
                                    public void onPhoneClick(View view, int position) {
                                        call_pos=position;
                                        if(isPermissionGranted()){
                                            call_action(requests.get(call_pos).getPhone_no());
                                        }
                                    }

                                    @Override
                                    public void onAvailClick(View view, int postion) {
                                        //do nothing for now
                                    }
                                });

                                mAdpater.setOnLoadMoreListener(new List_Adapter.OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        requests.add(null);
                                        mAdpater.notifyItemRemoved(requests.size());
                                        loadRequestBooks();
                                    }
                                });

                                recyclerView.setAdapter(mAdpater);

                                p_bar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                                p_bar.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                p_bar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
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
            if (ActivityCompat.checkSelfPermission(getActivity(),android.Manifest.permission.CALL_PHONE)
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
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action(requests.get(call_pos).getPhone_no());
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void call_action(String pos){
        String phnum = pos;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));
        startActivity(callIntent);
    }

}
