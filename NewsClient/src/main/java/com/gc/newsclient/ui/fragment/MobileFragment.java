package com.gc.newsclient.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.newsclient.R;
import com.gc.newsclient.adapter.MobileAdapter;
import com.gc.newsclient.ui.activity.NewsContentActivity;
import com.gc.newsclient.view.RecycleViewDivider;
import com.gc.newssplider.bean.NewsItem;
import com.gc.newssplider.biz.NewsItemBiz;
import com.gc.newssplider.util.URLUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 移动开发 SwipeRefreshLayout + RecyclerView 实现
 * @author gc
 */
public class MobileFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private MobileAdapter adapter;
    private boolean loading = false; // 判断是否在加载更多,避免重复请求网络

    private int currentPage = 1; // 当前页面
    private List<NewsItem> list = new ArrayList<NewsItem>(); // 数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_mobile);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_mobile);
        // 添加条目分隔线
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(),R.drawable.divider_mileage));

        // 设置进度动画的颜色
        refreshLayout.setColorSchemeResources(R.color.swipe_color_1);
        // 设置进度圈的大小,只有两个值:DEFAULT、LARGE
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        // true:下拉过程会自动缩放,200:下拉刷新的高度
        refreshLayout.setProgressViewEndTarget(true, 200);

        // 创建一个线性布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MobileAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

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

        // 检测滑动事件
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            // dx:在x方向滑动的值，这个值有正负, dy:在y方向滑动的值，这个值有正负
            // dx>0:右滑, dx<0:左滑, dy<0:上滑, dy>0:下滑
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 当前屏幕所看到的子项个数
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    Log.i("JAVA", "RecyclerView已经滑动到最下面了");
                    boolean isRefreshing = refreshLayout.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!loading){
                        // 这里使用postDelayed()方法模拟网络请求等延时操作,实际开发可去掉postDelayed()方法
                        refreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("JAVA", "RecyclerView开始加载更多了");
                                loading = true;
                                loadMoreData();
                            }
                        }, 1000);
                    }
                }
            }
        });

        //添加点击事件
        adapter.setOnItemClickListener(new MobileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                intent.putExtra("url", adapter.getList().get(position).getLink());
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });

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
                    list = new NewsItemBiz().getNewsItems(URLUtil.NEWS_TYPE_CLOUD, currentPage);
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
            }
        }
    };

}
