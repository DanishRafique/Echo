package in.co.echoindia.echo.HomePage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import in.co.echoindia.echo.R;

public class NewsActivity extends AppCompatActivity {

    WebView newsLink;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        bundle=getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Echo News");
        setSupportActionBar(toolbar);
        newsLink=(WebView)findViewById(R.id.news_web_view);
        newsLink.getSettings().setLoadsImagesAutomatically(true);
        newsLink.getSettings().setJavaScriptEnabled(true);
        newsLink.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        newsLink.loadUrl(bundle.getString("Link"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
