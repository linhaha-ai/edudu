package com.edudu.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edudu.ai.domain.UserMemory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户长期记忆 Mapper
 */
@Mapper
public interface UserMemoryMapper extends BaseMapper<UserMemory> {
}
