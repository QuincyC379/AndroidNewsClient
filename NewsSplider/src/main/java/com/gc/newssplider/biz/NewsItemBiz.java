package com.gc.newssplider.biz;

import com.gc.newssplider.bean.NewsItem;
import com.gc.newssplider.util.HtmlUtil;
import com.gc.newssplider.util.URLUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理NewItem的业务类
 * @author gc
 */
public class NewsItemBiz {
    /**
     * 获取新闻Item
     *
     * @param newsType    文章类型
     * @param currentPage 当前页码
     * @return
     * @throws Exception
     */
    public List<NewsItem> getNewsItems(int newsType, int currentPage) throws IOException {

        String urlStr = URLUtil.getNewsUrl(newsType, currentPage);
        String htmlStr = HtmlUtil.doGet(urlStr);

        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        NewsItem newsItem = null;

        /**
         * Jsoup只需先构建一个Document对象，然后就可以像使用js一个解析html了
         */
        // String作为输入源
        Document doc = Jsoup.parse(htmlStr);
        // 通过class获得元素
        Elements units = doc.getElementsByClass("unit");
        for (int i = 0; i < units.size(); i++) {
            newsItem = new NewsItem();
            newsItem.setNewsType(newsType);

            // Element是用来构建xml中节点的
            Element unit_ele = units.get(i);
            Element h1_ele = unit_ele.getElementsByTag("h1").get(0);
            Element h1_a_ele = h1_ele.child(0);
            String title = h1_a_ele.text();
            String href = h1_a_ele.attr("href");

            newsItem.setLink(href);
            newsItem.setTitle(title);

            Element h4_ele = unit_ele.getElementsByTag("h4").get(0);
            Element ago_ele = h4_ele.getElementsByClass("ago").get(0);
            String date = ago_ele.text();

            newsItem.setDate(date);

            Element dl_ele = unit_ele.getElementsByTag("dl").get(0);// dl
            Element dt_ele = dl_ele.child(0);// dt
            try {// 可能没有图片
                Element img_ele = dt_ele.child(0);
                String imgLink = img_ele.child(0).attr("src");
                newsItem.setImgLink(imgLink);
            } catch (IndexOutOfBoundsException e) {

            }
            Element content_ele = dl_ele.child(1);// dd
            String content = content_ele.text();
            newsItem.setContent(content);
            newsItems.add(newsItem);
        }
        return newsItems;
    }

}