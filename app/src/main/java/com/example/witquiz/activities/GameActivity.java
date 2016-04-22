package com.example.witquiz.activities;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.witquiz.R;
import com.example.witquiz.fragments.GameFragment;

public class GameActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this.getIntent().getExtras());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Bundle bundle;

        public final int POSITION_QUESTION = 0;

        public SectionsPagerAdapter(FragmentManager fm, Bundle bundle) {
            super(fm);

            this.bundle = bundle;
        }

        @Override
        public Fragment getItem(int position) {

            if(position == POSITION_QUESTION) {
                GameFragment fragment = new GameFragment();

                fragment.setArguments(bundle);

                return fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
