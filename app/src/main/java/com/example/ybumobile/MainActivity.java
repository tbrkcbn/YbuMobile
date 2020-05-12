package com.example.ybumobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    // Tab Menu Setting
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    PageAdapter pageAdapter;

    // JSoup Librariy Setting
    TextView fragment_red_txt_title;
    LinearLayout fragment_red_j_soup_pnl_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    public void init() {

        // tab_view_toolbar = findViewById(R.id.tab_view_toolbar);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.tab_view_view_pager);
        tabLayout = findViewById(R.id.tab_view_tab_layout);

        fragment_red_txt_title = findViewById(R.id.fragment_red_txt_title);
        fragment_red_j_soup_pnl_add = findViewById(R.id.fragment_red_j_soup_pnl_add);

        pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new ThreeFragment(), "Food List");
        pageAdapter.addFragment(new TwoFragment(), "Announce");
        pageAdapter.addFragment(new OneFragment(), "News");

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
