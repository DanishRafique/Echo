package in.co.echoindia.echo.User;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import in.co.echoindia.echo.HomePage.RepCentralFragment;
import in.co.echoindia.echo.HomePage.RepLocalFragment;
import in.co.echoindia.echo.HomePage.RepStateFragment;
import in.co.echoindia.echo.R;

public class ElectedRepresentativeActivity extends AppCompatActivity
        implements RepCentralFragment.OnFragmentInteractionListener,
        RepStateFragment.OnFragmentInteractionListener,
        RepLocalFragment.OnFragmentInteractionListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elected_representative);

        viewPager = (ViewPager) findViewById(R.id.rep_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.rep_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_rep);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //setupTabIcons();

        /*tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
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
        });*/
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_poll_blue);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_buzz_blue);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        RepCentralFragment repCentralFragment = new RepCentralFragment();
        RepStateFragment repStateFragment = new RepStateFragment();
        RepLocalFragment repLocalFragment = new RepLocalFragment();

        adapter.addFragment(repCentralFragment,"CENTRAL");
        adapter.addFragment(repStateFragment,"STATE");
        adapter.addFragment(repLocalFragment,"LOCAL");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

}
