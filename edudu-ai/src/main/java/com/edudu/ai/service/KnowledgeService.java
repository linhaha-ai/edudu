package com.edudu.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edudu.ai.domain.KnowledgeDocument;
import com.edudu.ai.mapper.KnowledgeDocumentMapper;
import com.edudu.ai.scraper.WebScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 知识库服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeService extends ServiceImpl<KnowledgeDocumentMapper, KnowledgeDocument> {

    private final WebScraper webScraper;

    /**
     * 导入知识文档
     */
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeDocument importDocument(String title, String content, String category) {
        KnowledgeDocument doc = new KnowledgeDocument();
        doc.setTitle(title);
        doc.setContent(content);
        doc.setCategory(category);
        doc.setStatus(1);
        save(doc);
        log.info("导入知识文档: id={}, title={}", doc.getId(), title);
        return doc;
    }

    /**
     * 从 URL 导入知识文档
     */
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeDocument importFromUrl(String url, String category) {
        log.info("从 URL 导入知识: url={}", url);
        String content = webScraper.scrape(url);
        String title = webScraper.getTitle(url);
        if (title == null || title.isEmpty()) {
            title = "从 " + url + " 导入的文档";
        }
        return importDocument(title, content, category);
    }

    /**
     * 获取启用的知识文档列表
     */
    public List<KnowledgeDocument> listEnabled() {
        return list(new LambdaQueryWrapper<KnowledgeDocument>()
                .eq(KnowledgeDocument::getStatus, 1));
    }

    /**
     * 更新文档状态
     */
    public boolean updateStatus(Long id, Integer status) {
        KnowledgeDocument doc = new KnowledgeDocument();
        doc.setId(id);
        doc.setStatus(status);
        return updateById(doc);
    }

    /**
     * 删除文档（逻辑删除）
     */
    public boolean deleteDocument(Long id) {
        return removeById(id);
    }

    /**
     * 重建向量索引（TODO: 实现向量存储逻辑）
     */
    public void rebuildIndex() {
        log.info("开始重建向量索引...");
        List<KnowledgeDocument> docs = listEnabled();
        log.info("索引重建完成，共 {} 篇文档", docs.size());
    }
}