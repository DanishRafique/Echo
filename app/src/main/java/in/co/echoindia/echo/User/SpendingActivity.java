package in.co.echoindia.echo.User;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import in.co.echoindia.echo.R;

public class SpendingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_spending);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.spending_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.spending_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        BudgetCentralFragment budgetCentralFragment = new BudgetCentralFragment();
        BudgetStateFragment budgetStateFragment = new BudgetStateFragment();
        BudgetLocalFragment budgetLocalFragment = new BudgetLocalFragment();

        adapter.addFragment(budgetCentralFragment,"CENTRAL");
        adapter.addFragment(budgetStateFragment,"STATE");
        adapter.addFragment(budgetLocalFragment,"LOCAL");

        viewPager.setAdapter(adapter);
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
