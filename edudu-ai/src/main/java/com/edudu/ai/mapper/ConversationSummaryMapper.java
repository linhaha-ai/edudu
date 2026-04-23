package com.edudu.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edudu.ai.domain.ConversationSummary;
import org.apache.ibatis.annotations.Mapper;

/**
 * 对话摘要 Mapper
 */
@Mapper
public interface ConversationSummaryMapper extends BaseMapper<ConversationSummary> {
}
