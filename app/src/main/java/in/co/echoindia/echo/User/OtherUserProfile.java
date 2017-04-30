package in.co.echoindia.echo.User;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.co.echoindia.echo.Model.PostDetailModel;
import in.co.echoindia.echo.R;

public class OtherUserProfile extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView userProfileImage;
    private static final String LOG_TAG = "OtherUserProfile";
    ListView userList;
    MyPostAdapter myAdapter;
    MyPostRepAdapter myRepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        userProfileImage=(ImageView)findViewById(R.id.user_profile_image);
        Bundle mBundle=getIntent().getExtras();
        Bundle userBundle=mBundle.getBundle("userPost");
        ArrayList<PostDetailModel> userPostDetail=(ArrayList<PostDetailModel>) userBundle.getSerializable("userPost");
        userList=(ListView)findViewById(R.id.user_list);
        collapsingToolbarLayout.setTitle(userBundle.getString("userName"));
        String userImage=userBundle.getString("userImage");
        Glide.with(this).load(userImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(userProfileImage);
        if(userBundle.getString("userType").equals("USER")) {
            myAdapter = new MyPostAdapter(this, userPostDetail);
            userList.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
        }
        else if(userBundle.getString("userType").equals("REP")) {
            myRepAdapter = new MyPostRepAdapter(this, userPostDetail);
            userList.setAdapter(myRepAdapter);
            myRepAdapter.notifyDataSetChanged();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
