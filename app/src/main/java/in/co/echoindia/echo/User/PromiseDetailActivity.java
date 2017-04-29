package in.co.echoindia.echo.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import in.co.echoindia.echo.Model.PromiseDetailModel;
import in.co.echoindia.echo.Model.PromiseModel;
import in.co.echoindia.echo.R;

public class PromiseDetailActivity extends AppCompatActivity {

    ArrayList <PromiseDetailModel> promiseDetailModelArrayList=new ArrayList<PromiseDetailModel>();
    PromiseDetailAdapter promiseDetailAdapter;
    ListView promiseDetailListView;
    private static final String LOG_TAG = "PromiseDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promise_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        promiseDetailListView=(ListView)findViewById(R.id.promise_detail_list_view);
        Bundle mBundle=getIntent().getExtras();
        Bundle repBundle=mBundle.getBundle("promiseObj");
        PromiseModel promiseModel=(PromiseModel)repBundle.getSerializable("promiseObj");
        promiseDetailModelArrayList=promiseModel.getPromiseDetail();
        Log.e(LOG_TAG,"Size of PromiseDetail "+promiseDetailModelArrayList.size());
        promiseDetailAdapter = new PromiseDetailAdapter(this, promiseDetailModelArrayList);
        promiseDetailListView.setAdapter(promiseDetailAdapter);
        promiseDetailAdapter.notifyDataSetChanged();
        toolbar.setTitle(promiseModel.getPromiseType());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
