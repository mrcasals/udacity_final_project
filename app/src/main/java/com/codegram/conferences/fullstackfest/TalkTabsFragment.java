package com.codegram.conferences.fullstackfest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codegram.conferences.fullstackfest.config.FullStackFestConfig;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TalkTabsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TalkTabsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TalkTabsFragment extends Fragment implements MaterialTabListener {
    private ViewPager mPager;
    MaterialTabHost mTabHost;
    ViewPagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk_tabs,container,false);

        mTabHost = (MaterialTabHost) view.findViewById(R.id.materialTabHost);
        mPager = (ViewPager) view.findViewById(R.id.viewpager );

        // init view pager
        mPagerAdapter = new ViewPagerAdapter(this.getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                mTabHost.setSelectedNavigationItem(position);

            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            mTabHost.addTab(
                    mTabHost.newTab()
                            .setText(mPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
        int actionBarColor = ((MaterialNavigationDrawer) getActivity()).getCurrentSection().getSectionColor();
        mTabHost.setPrimaryColor(actionBarColor);
        mTabHost.setAccentColor(Color.parseColor(FullStackFestConfig.FUTUREJS_COLOR));
        mTabHost.setTextColor(Color.parseColor("#212121"));

        return view;
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        // when the tab is clicked the pager swipe content to the tab position
        mPager.setCurrentItem(tab.getPosition());

    }
    @Override
    public void onTabReselected(MaterialTab tab) {}

    @Override
    public void onTabUnselected(MaterialTab tab) {}

    public int getNumberTabs() {
        return 4;
    }

    public CharSequence getCurrentTitle(int position) {
        return "Day " + Integer.toString(position + 1);
    }

    public Fragment getCurrentContent(int position) {
        return new TalkListFragment();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int num) {
            return getCurrentContent(num);
        }

        @Override
        public int getCount() {
            return getNumberTabs();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getCurrentTitle(position);
        }

    }
}
