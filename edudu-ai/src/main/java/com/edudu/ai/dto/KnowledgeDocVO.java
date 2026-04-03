package com.edudu.ai.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识文档视图对象
 */
@Data
public class KnowledgeDocVO {

    private Long id;
    private String title;
    private String category;
    private Integer status;
    private LocalDateTime createTime;
}