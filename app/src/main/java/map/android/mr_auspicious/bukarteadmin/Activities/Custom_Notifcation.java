package map.android.mr_auspicious.bukarteadmin.Activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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


public class Custom_Notifcation extends Activity {


    private CheckBox book_avail;
    private CheckBox order_confirmed;
    private CheckBox book_unavail;
    private CheckBox welcome_bukarte;
    private int not_id=0;
    private LinearLayout lin;
    private EditText cs_id;
    private Button send_notfication;
    private TextView dispatch_text;
    private String c_id ="";
    private static final String NOTIFICATION_URL = "https://www.bukarte.com/rest_server/autoNotification/API-KEY/123456";


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_notification);

        send_notfication= findViewById(R.id.send_notification);
        lin = findViewById(R.id.lin_layout);
        dispatch_text = findViewById(R.id.dispatch_text);

        book_avail =findViewById(R.id.req_book_avail);
        book_avail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (book_avail.isChecked()){
                    lin.setVisibility(View.VISIBLE);
                    not_id=1;
                    order_confirmed.setChecked(false);
                    book_unavail.setChecked(false);
                    welcome_bukarte.setChecked(false);
                }
            }
        });

        order_confirmed = findViewById(R.id.order_not);
        order_confirmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (order_confirmed.isChecked()){
                    lin.setVisibility(View.VISIBLE);
                    not_id = 2;
                    book_unavail.setChecked(false);
                    book_unavail.setChecked(false);
                    welcome_bukarte.setChecked(false);
                }
            }
        });

        book_unavail = findViewById(R.id.req_book_unavail);
        book_unavail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (book_unavail.isChecked()){
                    lin.setVisibility(View.VISIBLE);
                    not_id=3;
                    order_confirmed.setChecked(false);
                    book_avail.setChecked(false);
                    welcome_bukarte.setChecked(false);
                }
            }
        });

        welcome_bukarte = findViewById(R.id.welcome_not);
        welcome_bukarte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (welcome_bukarte.isChecked()){
                    lin.setVisibility(View.VISIBLE);
                    not_id=4;
                    order_confirmed.setChecked(false);
                    book_unavail.setChecked(false);
                    book_unavail.setChecked(false);
                }
            }
        });

        cs_id =findViewById(R.id.cs_id);
        cs_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                c_id = charSequence.toString();

                if(c_id.equals(""))
                    dispatch_text.setText("Customer ID Likho be");
                else
                    dispatch_text.setText("Notification Ready 2 Dispatch");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        send_notfication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c_id.equals("")){
                    Toast.makeText(Custom_Notifcation.this, "Areh Bhaiwa Customer ID Daalo re", Toast.LENGTH_SHORT).show();
                }else {
                    sendNotification();
                }
            }
        });
    }


    void sendNotification(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        HashMap<String,String> postParam = new HashMap<>();
        postParam.put("msg_code", String.valueOf(not_id));
        postParam.put("c_id",cs_id.getText().toString().trim());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                NOTIFICATION_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){
                                Toast.makeText(Custom_Notifcation.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Custom_Notifcation.this, "Connection Error", Toast.LENGTH_SHORT).show();
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
