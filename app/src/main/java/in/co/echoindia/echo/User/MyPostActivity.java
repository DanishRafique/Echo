package in.co.echoindia.echo.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.Model.RepDetailModel;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class MyPostActivity extends AppCompatActivity {
    ListView myListView;
    ArrayList<PostDetailModel> myListArray = new ArrayList<>();

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Type type;
    MyPostAdapter myAdapter;
    MyPostRepAdapter myRepAdapter;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout mAppBarLayout;
    ImageView myProfileImage;
    UserDetailsModel mUserDetailModel;
    RepDetailModel mRepDetailModel;
    private static final String LOG_TAG = "MyPostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myListView=(ListView)findViewById(R.id.my_list);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        Bundle mBundle=getIntent().getExtras();
        Bundle userBundle=mBundle.getBundle("userPost");
        myListArray=(ArrayList<PostDetailModel>) userBundle.getSerializable("userPost");


        myProfileImage=(ImageView)findViewById(R.id.my_profile_image);
        if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("USER")){
            mUserDetailModel = new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), UserDetailsModel.class);
            Glide.with(this).load(mUserDetailModel.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(myProfileImage);
            collapsingToolbarLayout.setTitle(mUserDetailModel.getFirstName()+" "+mUserDetailModel.getLastName());
            if(myListArray!=null) {
                Collections.sort(myListArray, new PostComparator());
                myAdapter = new MyPostAdapter(this, myListArray);
                myListView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }
        }
        else if(sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"").equals("REP")) {
            mRepDetailModel = new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), RepDetailModel.class);
            Glide.with(this).load(mRepDetailModel.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(myProfileImage);
            collapsingToolbarLayout.setTitle(mRepDetailModel.getFirstName() + " " + mRepDetailModel.getLastName());
            if (myListArray!=null) {
                Collections.sort(myListArray, new PostComparator());
                myRepAdapter = new MyPostRepAdapter(this, myListArray);
                myListView.setAdapter(myRepAdapter);
                myRepAdapter.notifyDataSetChanged();
            }
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.my_profile_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postIntent=new Intent(MyPostActivity.this, PostActivity.class);
                startActivity(postIntent);
                finish();
            }
        });

    }
    public class PostComparator implements Comparator<PostDetailModel> {

        @Override
        public int compare(PostDetailModel lhs, PostDetailModel rhs) {

            return Integer.parseInt(lhs.getPostId())<Integer.parseInt(rhs.getPostId()) ? 1:(Integer.parseInt(lhs.getPostId())==Integer.parseInt(rhs.getPostId())?0:-1);
        }
    }

}
