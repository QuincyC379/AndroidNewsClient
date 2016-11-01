package com.gc.newsclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gc.newsclient.adapter.MyFragmentPagerAdapter;
import com.gc.newsclient.ui.fragment.AboutFragment;
import com.gc.newsclient.ui.fragment.MobileFragment;
import com.gc.newsclient.ui.fragment.NewsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gc
 */
public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private List<Fragment> listFragment;
    private List<String> listTitle;

    /**
     * 此处的众多Fragment由开发者自定义替换
     */
    private NewsFragment newsFragment;
    private MobileFragment mobileFragment;
    private AboutFragment aboutFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
    }

    /**
     * 初始化各控件
     */
    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // 初始化各fragment
        newsFragment = new NewsFragment();
        mobileFragment = new MobileFragment();
        aboutFragment = new AboutFragment();

        // 将fragment装进列表中
        listFragment = new ArrayList<>();
        listFragment.add(newsFragment);
        listFragment.add(mobileFragment);
        listFragment.add(aboutFragment);

        // 将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        listTitle = new ArrayList<>();
        listTitle.add("业界");
        listTitle.add("移动开发");
        listTitle.add("关于项目");

        // tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // 设置TabLayout的模式
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // 为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(2)));

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), listFragment, listTitle);

        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
