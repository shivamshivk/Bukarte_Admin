package map.android.mr_auspicious.bukarteadmin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import map.android.mr_auspicious.bukarteadmin.R;

public class Web_View_Activity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar p_bar;
    private String tracking_url = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web__view_);

        Intent intent= getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            tracking_url = extras.getString("tracking_url");
        }

        p_bar = findViewById(R.id.p_bar);

        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(tracking_url);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
                p_bar.setVisibility(View.GONE);
            }
        });

    }
}
