package com.edudu.api.controller.ai;

import com.edudu.ai.dto.KnowledgeDocRequest;
import com.edudu.ai.dto.KnowledgeDocVO;
import com.edudu.ai.dto.UrlImportRequest;
import com.edudu.ai.service.KnowledgeService;
import com.edudu.common.core.domain.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/ai/knowledge")
@RequiredArgsConstructor
@Tag(name = "知识库管理接口")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    /**
     * 导入知识文档
     */
    @PostMapping("/import")
    @Operation(summary = "导入知识文档", description = "手动导入知识文档")
    public AjaxResult importDocument(@RequestBody KnowledgeDocRequest request) {
        log.info("导入知识文档: title={}", request.getTitle());
        var doc = knowledgeService.importDocument(
                request.getTitle(),
                request.getContent(),
                request.getCategory()
        );
        return AjaxResult.success(toVO(doc));
    }

    /**
     * 从 URL 导入知识
     */
    @PostMapping("/import-url")
    @Operation(summary = "从URL导入", description = "从网页URL导入知识文档")
    public AjaxResult importFromUrl(@RequestBody UrlImportRequest request) {
        log.info("从URL导入知识: url={}", request.getUrl());
        var doc = knowledgeService.importFromUrl(request.getUrl(), request.getCategory());
        return AjaxResult.success(toVO(doc));
    }

    /**
     * 删除知识文档
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除知识文档")
    public AjaxResult delete(
            @Parameter(description = "文档ID") @PathVariable Long id) {
        log.info("删除知识文档: id={}", id);
        boolean result = knowledgeService.deleteDocument(id);
        return result ? AjaxResult.success() : AjaxResult.error("删除失败");
    }

    /**
     * 获取知识文档列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取知识文档列表")
    public AjaxResult list(
            @Parameter(description = "分类") @RequestParam(required = false) String category) {
        List<KnowledgeDocVO> list = knowledgeService.list()
                .stream()
                .map(this::toVO)
                .toList();
        return AjaxResult.success(list);
    }

    /**
     * 重建向量索引
     */
    @PostMapping("/rebuild")
    @Operation(summary = "重建向量索引")
    public AjaxResult rebuild() {
        log.info("重建向量索引");
        knowledgeService.rebuildIndex();
        return AjaxResult.success("索引重建完成");
    }

    /**
     * 实体转 VO
     */
    private KnowledgeDocVO toVO(com.edudu.ai.domain.KnowledgeDocument doc) {
        KnowledgeDocVO vo = new KnowledgeDocVO();
        vo.setId(doc.getId());
        vo.setTitle(doc.getTitle());
        vo.setCategory(doc.getCategory());
        vo.setStatus(doc.getStatus());
        vo.setCreateTime(doc.getCreateTime());
        return vo;
    }
}