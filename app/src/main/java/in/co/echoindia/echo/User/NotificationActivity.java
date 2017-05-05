package in.co.echoindia.echo.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.co.echoindia.echo.Model.NotificationModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class NotificationActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    NotificationAdapter notificationAdapter;
    ListView notificationListView;
    ArrayList<NotificationModel> notificationList=new ArrayList<>();
    private static final String LOG_TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        notificationListView=(ListView)findViewById(R.id.notification_list);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        Type type= new TypeToken<ArrayList<NotificationModel>>() {}.getType();
        notificationList = new Gson().fromJson(sharedpreferences.getString(Constants.MY_NOTIFICATION, ""), type);
        Log.e(LOG_TAG,"home Element Count "+notificationList.size());
        Collections.sort(notificationList,new PostComparator());
        notificationAdapter = new NotificationAdapter(this, notificationList);
        notificationListView.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
    }

    public class PostComparator implements Comparator<NotificationModel> {

        @Override
        public int compare(NotificationModel lhs, NotificationModel rhs) {

            return Integer.parseInt(lhs.getPostID())<Integer.parseInt(rhs.getPostID()) ? 1:(Integer.parseInt(lhs.getPostID())==Integer.parseInt(rhs.getPostID())?0:-1);
        }
    }
}
