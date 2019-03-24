package map.android.mr_auspicious.bukarteadmin.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import map.android.mr_auspicious.bukarteadmin.Applications.AppController;
import map.android.mr_auspicious.bukarteadmin.R;


public class Fragment_Email extends Fragment {

    private static final String EMAIL_URL = "https://www.bukarte.com/rest_server/sendCustomMail/API-KEY/123456";
    private static final String KEY_FROM = "from";
    private static final String KEY_TO = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_LINE_1 = "line_1";
    private static final String KEY_LINE_2 = "line_2";
    private static final String KEY_PUNCH_POINT = "punch_line";
    private static final String KEY_URL = "url";
    private static final String KEY_URL_TEXT = "url_text";
    private static final String KEY_FROM_NAME = "from_name";
    private View rootview;
    private EditText input_from;
    private EditText input_to;
    private EditText input_name;
    private EditText input_subject;
    private EditText input_line_1;
    private EditText input_line_2;
    private EditText input_punch_point;
    private EditText input_url;
    private ProgressDialog mProgressDialog;
    private Button send_email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_email, container, false);

        input_from = (EditText) rootview.findViewById(R.id.inpuy_from);
        input_to = (EditText) rootview.findViewById(R.id.input_to);
        input_name = (EditText) rootview.findViewById(R.id.input_name);
        input_subject = (EditText) rootview.findViewById(R.id.input_subject);
        input_line_1 = (EditText) rootview.findViewById(R.id.input_line);
        input_line_2 = (EditText) rootview.findViewById(R.id.input_line2);
        input_punch_point = (EditText) rootview.findViewById(R.id.input_punch_line);
        input_url = (EditText) rootview.findViewById(R.id.input_url);
        send_email = rootview.findViewById(R.id.send_email);

        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!input_from.getText().toString().trim().isEmpty() && !input_to.getText().toString().trim().isEmpty() && !input_name.getText().toString().isEmpty()&& !input_subject.getText().toString().isEmpty() && !input_line_1.getText().toString().isEmpty() && !input_line_2.getText().toString().isEmpty() && !input_punch_point.getText().toString().isEmpty() && !input_url.getText().toString().trim().isEmpty())
                    mailIT();
                else
                    Toast.makeText(getActivity(), "Fill it out", Toast.LENGTH_SHORT).show();
            }
        });

        return rootview;
    }

    void mailIT(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        HashMap<String,String> postParam = new HashMap<>();
        postParam.put(KEY_NAME,input_name.getText().toString().trim());
        postParam.put(KEY_SUBJECT,input_subject.getText().toString().trim());
        postParam.put(KEY_TO,input_to.getText().toString().trim());
        postParam.put(KEY_FROM,input_from.getText().toString().trim());
        postParam.put(KEY_FROM_NAME,"Shivam");
        postParam.put(KEY_LINE_1,input_line_1.getText().toString().trim());
        postParam.put(KEY_LINE_2,input_line_2.getText().toString().trim());
        postParam.put(KEY_PUNCH_POINT,input_punch_point.getText().toString().trim());
        postParam.put(KEY_URL,input_url.getText().toString().trim());
        postParam.put(KEY_URL_TEXT,"");


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                EMAIL_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){
                                Toast.makeText(getActivity(), "Email Sent", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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