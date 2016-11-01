package com.gc.newssplider;

import com.gc.newssplider.bean.NewsItem;
import com.gc.newssplider.biz.NewsItemBiz;
import com.gc.newssplider.util.URLUtil;

import org.junit.Test;

import java.util.List;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void getNewsItem() throws Exception {
        NewsItemBiz biz = new NewsItemBiz();
        int currentPage = 1;
        try {
            // 业界
            List<NewsItem> newsItems = biz.getNewsItems(URLUtil.NEWS_TYPE_NEWS, currentPage);
            for (NewsItem item : newsItems) {
                System.out.println(item);
            }
            System.out.println("-----------news end-----------");

            // 移动开发
            newsItems = biz.getNewsItems(URLUtil.NEWS_TYPE_MOBILE, currentPage);
            for (NewsItem item : newsItems) {
                System.out.println(item);
            }
            System.out.println("-----------mobile end-----------");

            // 云计算
            newsItems = biz.getNewsItems(URLUtil.NEWS_TYPE_CLOUD, currentPage);
            for (NewsItem item : newsItems) {
                System.out.println(item);
            }
            System.out.println("-----------cloud end-----------");

            // 软件研发
            newsItems = biz.getNewsItems(URLUtil.NEWS_TYPE_SD, currentPage);
            for (NewsItem item : newsItems) {
                System.out.println(item);
            }
            System.out.println("-----------sd end-----------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
