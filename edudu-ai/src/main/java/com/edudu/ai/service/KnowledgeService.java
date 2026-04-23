package com.edudu.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edudu.ai.domain.KnowledgeDocument;
import com.edudu.ai.mapper.KnowledgeDocumentMapper;
import com.edudu.ai.scraper.WebScraper;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
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
    private final EmbeddingStoreIngestor embeddingStoreIngestor;

    /**
     * 导入知识文档
     */
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeDocument importDocument(String title, String content, String category) {
        // 输入验证
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("文档标题不能为空");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("文档内容不能为空");
        }

        KnowledgeDocument doc = new KnowledgeDocument();
        doc.setTitle(title.trim());
        doc.setContent(content.trim());
        doc.setCategory(category != null ? category.trim() : null);
        doc.setStatus(1);
        save(doc);

        // 自动向量化
        ingestDocument(doc);

        log.info("导入知识文档: id={}, title={}", doc.getId(), title);
        return doc;
    }

    /**
     * 从 URL 导入知识文档
     */
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeDocument importFromUrl(String url, String category) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL不能为空");
        }

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
        if (id == null) {
            return false;
        }
        KnowledgeDocument doc = new KnowledgeDocument();
        doc.setId(id);
        doc.setStatus(status);
        return updateById(doc);
    }

    /**
     * 删除文档（逻辑删除）
     */
    public boolean deleteDocument(Long id) {
        if (id == null) {
            return false;
        }
        return removeById(id);
    }

    /**
     * 重建向量索引
     */
    public void rebuildIndex() {
        log.info("开始重建向量索引...");
        List<KnowledgeDocument> docs = listEnabled();

        for (KnowledgeDocument doc : docs) {
            ingestDocument(doc);
        }

        log.info("索引重建完成，共 {} 篇文档", docs.size());
    }

    /**
     * 将文档向量化存储
     */
    private void ingestDocument(KnowledgeDocument doc) {
        try {
            Document document = Document.from(
                    doc.getContent(),
                    Metadata.from("docId", doc.getId().toString())
                            .add("title", doc.getTitle())
                            .add("category", doc.getCategory() != null ? doc.getCategory() : ""));

            embeddingStoreIngestor.ingest(document);
            log.debug("文档向量化完成: docId={}, title={}", doc.getId(), doc.getTitle());
        } catch (Exception e) {
            log.error("文档向量化失败: docId={}, title={}", doc.getId(), doc.getTitle(), e);
        }
    }
}
