package com.edudu.ai.scraper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 网页爬虫工具
 */
@Slf4j
@Component
public class WebScraper {

    private static final int TIMEOUT_MS = 10000;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";

    /**
     * 从 URL 获取页面内容
     *
     * @param url 目标 URL
     * @return 页面文本内容
     */
    public String scrape(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MS)
                    .get();

            // 移除脚本和样式
            doc.select("script, style, nav, footer, header").remove();

            // 获取主体内容
            String text = doc.body().text();

            // 清理空白
            text = text.replaceAll("\\s+", " ").trim();

            log.info("成功爬取 URL: {}, 长度: {}", url, text.length());
            return text;
        } catch (IOException e) {
            log.error("爬取失败: {}", url, e);
            throw new RuntimeException("爬取网页失败: " + e.getMessage());
        }
    }

    /**
     * 从 URL 获取页面标题
     */
    public String getTitle(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MS)
                    .get();
            return doc.title();
        } catch (IOException e) {
            log.warn("获取标题失败: {}", url, e);
            return null;
        }
    }

    /**
     * 安全清理 HTML 内容
     */
    public String cleanHtml(String html) {
        return Jsoup.clean(html, Safelist.none());
    }
}