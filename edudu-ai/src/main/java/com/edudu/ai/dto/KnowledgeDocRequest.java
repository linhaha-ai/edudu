package com.edudu.ai.dto;

import lombok.Data;

/**
 * 知识文档导入请求
 */
@Data
public class KnowledgeDocRequest {

    private String title;
    private String content;
    private String category;
}