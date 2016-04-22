package com.example.witquiz.activities;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.witquiz.R;
import com.example.witquiz.fragments.GameFragment;
import com.example.witquiz.fragments.SecondGameFragment;

public class GameActivity extends AppCompatActivity implements GameFragment.SendSummaryInterface {

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

    @Override
    public void sendSummary(int allQuestions, int currentQuestion) {
        SecondGameFragment fragment = (SecondGameFragment) getSupportFragmentManager().findFragmentById(R.id.pager);

        if(fragment != null)
            fragment.setSummary(allQuestions, currentQuestion);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Bundle bundle;

        public final int POSITION_QUESTION = 0;
        public final int POSITION_SUMMARY  = 1;

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
            else { //if (position == POSITION_SUMMARY)
                SecondGameFragment fragment = new SecondGameFragment();

            fragment.setArguments(bundle);

            return fragment;
        }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
