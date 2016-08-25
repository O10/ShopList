package com.example.shoplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.shoplist.views.NonSwipeViewPager;

public class ShopListActivity extends AppCompatActivity implements ItemsListFragment.ListFragmentInterface {
    static final int NUM_TABS = 2;

    private NonSwipeViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FloatingActionButton floatingButton;

    private Animation showFabAnimation, hideFabAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        viewPager = (NonSwipeViewPager) findViewById(R.id.v_pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        floatingButton = (FloatingActionButton) findViewById(R.id.fab);

        viewPager.setAdapter(new ItemsViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.sho_acc_title));
        initListeners();
        initAnimations();
    }

    private void initAnimations() {
        hideFabAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        hideFabAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingButton.setVisibility(View.GONE);
                floatingButton.setEnabled(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        showFabAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        showFabAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                floatingButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingButton.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initListeners() {
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopListActivity.this, SingleItemActivity.class));
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    floatingButton.startAnimation(hideFabAnimation);
                } else {
                    floatingButton.startAnimation(showFabAnimation);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onContextualUp() {
        tabLayout.setVisibility(View.GONE);
        floatingButton.startAnimation(hideFabAnimation);
        viewPager.setShouldSwipe(false);
    }

    @Override
    public void onContextualDown() {
        tabLayout.setVisibility(View.VISIBLE);
        floatingButton.startAnimation(showFabAnimation);
        viewPager.setShouldSwipe(true);
    }

    /**
     * Adapter showing 2 pages : current and archived shopping items.
     */
    private class ItemsViewPagerAdapter extends FragmentPagerAdapter {

        public ItemsViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_TABS;
        }

        @Override
        public Fragment getItem(int position) {
            return ItemsListFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return ShopListActivity.this.getString(R.string.tab_current);
                case 1:
                default:
                    return ShopListActivity.this.getString(R.string.tab_archived);

            }
        }
    }
}
