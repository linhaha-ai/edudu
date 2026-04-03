package com.edudu.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edudu.ai.domain.KnowledgeDocument;
import org.apache.ibatis.annotations.Mapper;

/**
 * 知识文档 Mapper
 */
@Mapper
public interface KnowledgeDocumentMapper extends BaseMapper<KnowledgeDocument> {
}