package in.co.echoindia.echo.HomePage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.echoindia.echo.Model.UserDetailsModel;
import in.co.echoindia.echo.R;
import in.co.echoindia.echo.User.AboutUsActivity;
import in.co.echoindia.echo.User.ContactUsActivity;
import in.co.echoindia.echo.User.DevelopmentActivity;
import in.co.echoindia.echo.User.ElectedRepresentativeActivity;
import in.co.echoindia.echo.User.LoginActivity;
import in.co.echoindia.echo.User.MyAccountActivity;
import in.co.echoindia.echo.User.MyProfileActivity;
import in.co.echoindia.echo.User.SettingsActivity;
import in.co.echoindia.echo.User.SpendingActivity;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.Constants;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
            CircleImageView navUserImage;
            TextView navFullName,navUserName;
            private ProgressDialog pDialog;
            UserDetailsModel userDetailsModel;
    private static final String LOG_TAG = "HomePageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Echo");
        setSupportActionBar(toolbar);
        pDialog = new ProgressDialog(this);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setupTabIcons();

        View hView =  navigationView.getHeaderView(0);

        navUserImage=(CircleImageView) hView.findViewById(R.id.nav_user_image);
        navFullName=(TextView)hView.findViewById(R.id.nav_full_name);
        navUserName=(TextView)hView.findViewById(R.id.nav_user_name);
        userDetailsModel = new Gson().fromJson(sharedpreferences.getString(Constants.SETTINGS_OBJ_USER, ""), UserDetailsModel.class);
        Glide.with(this).load(userDetailsModel.getUserPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(navUserImage);
        navFullName.setText(capitalizeFirstLetter(userDetailsModel.getFirstName())+" "+capitalizeFirstLetter(userDetailsModel.getLastName()));
        navUserName.setText(userDetailsModel.getUserName());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (Build.VERSION.SDK_INT >= 23) {
                    tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getIcon().setColorFilter(getColor(R.color.tab_unselected), PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

            public String capitalizeFirstLetter(String original) {
                if (original == null || original.length() == 0) {
                    return original;
                }
                return original.substring(0, 1).toUpperCase() + original.substring(1);
            }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        NewsFragment mNewsFragment = new NewsFragment();
        PollFragment mPollFragment = new PollFragment();
        HomeFragment mHomeFragment = new HomeFragment();
        BuzzFragment mBuzzFragment=new BuzzFragment();

        adapter.addFragment(mHomeFragment,"HOME");
        adapter.addFragment(mBuzzFragment,"BUZZ");
        adapter.addFragment(mPollFragment,"POLL");
        adapter.addFragment(mNewsFragment,"NEWS");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i =new Intent(HomePageActivity.this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_my_account){
            Intent i = new Intent(HomePageActivity.this, MyAccountActivity.class);
            startActivity(i);
        } else if(id == R.id.nav_elected_representative){
            Intent i = new Intent(HomePageActivity.this, ElectedRepresentativeActivity.class);
            startActivity(i);
        } else if(id == R.id.nav_development_in_progress){
            Intent i = new Intent(HomePageActivity.this, DevelopmentActivity.class);
            startActivity(i);
        } else if(id == R.id.nav_spending){
            Intent i = new Intent(HomePageActivity.this, SpendingActivity.class);
            startActivity(i);
        } else if(id == R.id.nav_about_us){
            Intent i = new Intent(HomePageActivity.this, AboutUsActivity.class);
            startActivity(i);
        } else if(id == R.id.nav_contact_us){
            Intent i = new Intent(HomePageActivity.this,ContactUsActivity.class);
            startActivity(i);
        } else if(id == R.id.nav_log_out){
            ExecuteLogout mExecuteLogout=new ExecuteLogout();
            mExecuteLogout.execute();
        } else if(id == R.id.nav_my_profile){
            Intent i = new Intent(HomePageActivity.this,MyProfileActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_poll_blue);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_news_blue);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_buzz_blue);
    }

    private void setWorkLogOut() {
        Log.e(LOG_TAG,sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_TYPE,"")+" .");
        editor.putString(Constants.SETTINGS_IS_LOGGED_TYPE, "");
        editor.putBoolean(Constants.SETTINGS_IS_LOGGED, false);
        editor.putString(Constants.SETTINGS_IS_LOGGED_USER_CODE, "");
        editor.putString(Constants.SETTINGS_OBJ_USER,"");
        editor.commit();
        Intent loginPathIntent = new Intent(HomePageActivity.this, LoginActivity.class);
        loginPathIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginPathIntent);
        HomePageActivity.this.finish();
    }

    class ExecuteLogout extends AsyncTask {

                String url_user_logout = "http://echoindia.co.in/php/UserLogout.php";
                String userNameStr=sharedpreferences.getString(Constants.SETTINGS_IS_LOGGED_USER_CODE,"");
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog.show();
                    pDialog.setMessage("Please Wait");
                    pDialog.setCancelable(true);
                }

                @Override
                protected Object doInBackground(Object[] params) {

                    BufferedReader bufferedReader = null;
                    try {
                        URL url = new URL(url_user_logout);
                        JSONObject postDataParams = new JSONObject();
                        postDataParams.put("username",userNameStr);
                        Log.e(LOG_TAG,"URL"+ url_user_logout);
                        Log.e(LOG_TAG,"PostParam"+postDataParams.toString());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(15000 /* milliseconds */);
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        OutputStream os = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
                        writer.write(AppUtil.getPostDataString(postDataParams));
                        writer.flush();
                        writer.close();
                        os.close();
                        int responseCode = conn.getResponseCode();
                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader( conn.getInputStream()));
                            StringBuffer sb = new StringBuffer("");

                            String line = "";
                            while ((line = in.readLine()) != null) {
                                sb.append(line);
                                break;
                            }
                            in.close();
                            Log.e(LOG_TAG,sb.toString());
                            return sb.toString();

                        } else {
                            return new String("false : " + responseCode);
                        }
                    } catch (Exception ex) {
                        return null;
                    } finally {
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    pDialog.dismiss();
                    Log.e(LOG_TAG,"Logout "+o.toString());
                    setWorkLogOut();
                }
            }

            @Override
            protected void onPause() {
                super.onPause();
                if(pDialog!=null)
                    pDialog.dismiss();
            }
        }
