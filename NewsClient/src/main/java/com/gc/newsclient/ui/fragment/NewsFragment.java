package com.gc.newsclient.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gc.newsclient.R;
import com.gc.newsclient.adapter.NewsAdapter;
import com.gc.newsclient.ui.activity.NewsContentActivity;
import com.gc.newsclient.view.RefreshLayout;
import com.gc.newssplider.bean.NewsItem;
import com.gc.newssplider.biz.NewsItemBiz;
import com.gc.newssplider.util.URLUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 业界 SwipeRefreshLayout + Listview 实现
 *
 * @author gc
 */
public class NewsFragment extends Fragment {

    private RefreshLayout refreshLayout;
    private ListView listView;
    private NewsAdapter adapter;
    private boolean loading = false; // 判断是否在加载更多,避免重复请求网络

    private int currentPage = 1; // 当前页面
    private List<NewsItem> list = new ArrayList<NewsItem>(); // 数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        refreshLayout = (RefreshLayout) view.findViewById(R.id.srl_news);
        listView = (ListView) view.findViewById(R.id.lv_news);

        // 设置进度动画的颜色
        refreshLayout.setColorSchemeResources(R.color.swipe_color_1);
        // 设置进度圈的大小,只有两个值:DEFAULT、LARGE
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        // true:下拉过程会自动缩放,200:下拉刷新的高度
        refreshLayout.setProgressViewEndTarget(true, 200);

        // 进入页面就执行下拉动画
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });

        // 设置手势滑动监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 下拉刷新操作
                getData();
            }
        });

        // 上拉加载更多操作
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                Log.i("JAVA", "ListView已经滑动到最下面了");
                if (!loading) {
                    // 这里使用postDelayed()方法模拟网络请求等延时操作,实际开发可去掉postDelayed()方法
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("JAVA", "ListView开始加载更多了");
                            loading = true;
                            loadMoreData();
                        }
                    }, 1000);
                }
            }
        });

        // ListView条目点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                intent.putExtra("url", adapter.getList().get(position).getLink());
                startActivity(intent);
            }
        });

        adapter = new NewsAdapter(getContext(), list);
        listView.setAdapter(adapter);
        return view;
    }

    /**
     * 获取网络数据
     */
    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = new NewsItemBiz().getNewsItems(URLUtil.NEWS_TYPE_NEWS, currentPage);
                    handler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 加载更多操作
     */
    private void loadMoreData() {
        currentPage++;
        list.clear();
        getData();
        handler.sendEmptyMessage(2);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                    loading = false;
                    // 请求完成结束刷新状态
                    refreshLayout.setRefreshing(false);
                    break;
                case 2:
                    // 上拉加载更多请求完成结束刷新状态
                    refreshLayout.setLoading(false);
                    break;
            }
        }
    };

}
