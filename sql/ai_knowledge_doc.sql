-- AI知识库文档表
CREATE TABLE IF NOT EXISTS edu_ai_knowledge_doc (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(255) NOT NULL COMMENT '文档标题',
    content     TEXT NOT NULL COMMENT '文档内容',
    category    VARCHAR(100) DEFAULT NULL COMMENT '分类',
    status      TINYINT DEFAULT 1 COMMENT '状态: 0=禁用 1=启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI知识库文档表';