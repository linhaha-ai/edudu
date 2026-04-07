package com.edudu.edu.service;

import java.util.List;
import com.edudu.edu.domain.EduAiConfig;

/**
 * AI配置Service接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface IEduAiConfigService 
{
    /**
     * 查询AI配置
     * 
     * @param id AI配置主键
     * @return AI配置
     */
    public EduAiConfig selectEduAiConfigById(Long id);

    /**
     * 查询AI配置列表
     * 
     * @param eduAiConfig AI配置
     * @return AI配置集合
     */
    public List<EduAiConfig> selectEduAiConfigList(EduAiConfig eduAiConfig);

    /**
     * 新增AI配置
     * 
     * @param eduAiConfig AI配置
     * @return 结果
     */
    public int insertEduAiConfig(EduAiConfig eduAiConfig);

    /**
     * 修改AI配置
     * 
     * @param eduAiConfig AI配置
     * @return 结果
     */
    public int updateEduAiConfig(EduAiConfig eduAiConfig);

    /**
     * 批量删除AI配置
     * 
     * @param ids 需要删除的AI配置主键集合
     * @return 结果
     */
    public int deleteEduAiConfigByIds(Long[] ids);

    /**
     * 删除AI配置信息
     * 
     * @param id AI配置主键
     * @return 结果
     */
    public int deleteEduAiConfigById(Long id);
}
