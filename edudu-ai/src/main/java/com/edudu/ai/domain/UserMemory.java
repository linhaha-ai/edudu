package com.edudu.ai.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户长期记忆实体
 */
@Data
@TableName("edu_ai_user_memory")
public class UserMemory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String memoryKey;

    private String memoryValue;

    private String source;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
