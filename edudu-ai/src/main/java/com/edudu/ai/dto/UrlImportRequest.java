package com.edudu.ai.dto;

import lombok.Data;

/**
 * URL 导入请求
 */
@Data
public class UrlImportRequest {

    private String url;
    private String category;
}