package map.android.mr_auspicious.bukarteadmin.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import map.android.mr_auspicious.bukarteadmin.Adapters.Search_Adapter;
import map.android.mr_auspicious.bukarteadmin.Applications.AppController;
import map.android.mr_auspicious.bukarteadmin.Model.Search;
import map.android.mr_auspicious.bukarteadmin.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search_Terms extends Fragment {


    private static final String SEARCH_URL = "https://developers.bukarte.com/example_api/allSearchTerm/API-KEY/123456";
    private SwipeRefreshLayout refreshLayout;
    private View rootview;
    private ProgressBar p_bar;
    private RecyclerView recyclerView;
    private List<Search> searches = new ArrayList<>();
    private Search_Adapter mAdpater;

    public Search_Terms() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.activity_search__terms, container, false);

        refreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.refresh_);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                p_bar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                getSearchTerms();
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

        getSearchTerms();
    }

    void getSearchTerms(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                SEARCH_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("userData");

                                for(int i =0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String term = jsonObject.getString("search_term");
                                    String date_time = jsonObject.getString("st_time");

                                    searches.add(new Search(term,date_time));
                                }


                                mAdpater = new Search_Adapter(getActivity(),searches);

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
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


}
