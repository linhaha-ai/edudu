package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduAiConfigMapper;
import com.edudu.edu.domain.EduAiConfig;
import com.edudu.edu.service.IEduAiConfigService;

/**
 * AI配置Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduAiConfigServiceImpl implements IEduAiConfigService 
{
    @Autowired
    private EduAiConfigMapper eduAiConfigMapper;

    /**
     * 查询AI配置
     * 
     * @param id AI配置主键
     * @return AI配置
     */
    @Override
    public EduAiConfig selectEduAiConfigById(Long id)
    {
        return eduAiConfigMapper.selectEduAiConfigById(id);
    }

    /**
     * 查询AI配置列表
     * 
     * @param eduAiConfig AI配置
     * @return AI配置
     */
    @Override
    public List<EduAiConfig> selectEduAiConfigList(EduAiConfig eduAiConfig)
    {
        return eduAiConfigMapper.selectEduAiConfigList(eduAiConfig);
    }

    /**
     * 新增AI配置
     * 
     * @param eduAiConfig AI配置
     * @return 结果
     */
    @Override
    public int insertEduAiConfig(EduAiConfig eduAiConfig)
    {
        eduAiConfig.setCreateTime(DateUtils.getNowDate());
        return eduAiConfigMapper.insertEduAiConfig(eduAiConfig);
    }

    /**
     * 修改AI配置
     * 
     * @param eduAiConfig AI配置
     * @return 结果
     */
    @Override
    public int updateEduAiConfig(EduAiConfig eduAiConfig)
    {
        eduAiConfig.setUpdateTime(DateUtils.getNowDate());
        return eduAiConfigMapper.updateEduAiConfig(eduAiConfig);
    }

    /**
     * 批量删除AI配置
     * 
     * @param ids 需要删除的AI配置主键
     * @return 结果
     */
    @Override
    public int deleteEduAiConfigByIds(Long[] ids)
    {
        return eduAiConfigMapper.deleteEduAiConfigByIds(ids);
    }

    /**
     * 删除AI配置信息
     * 
     * @param id AI配置主键
     * @return 结果
     */
    @Override
    public int deleteEduAiConfigById(Long id)
    {
        return eduAiConfigMapper.deleteEduAiConfigById(id);
    }
}
