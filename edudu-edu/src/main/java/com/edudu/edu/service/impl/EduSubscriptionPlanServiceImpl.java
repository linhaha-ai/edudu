package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduSubscriptionPlanMapper;
import com.edudu.edu.domain.EduSubscriptionPlan;
import com.edudu.edu.service.IEduSubscriptionPlanService;

/**
 * 订阅计划配置Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduSubscriptionPlanServiceImpl implements IEduSubscriptionPlanService 
{
    @Autowired
    private EduSubscriptionPlanMapper eduSubscriptionPlanMapper;

    /**
     * 查询订阅计划配置
     * 
     * @param id 订阅计划配置主键
     * @return 订阅计划配置
     */
    @Override
    public EduSubscriptionPlan selectEduSubscriptionPlanById(Long id)
    {
        return eduSubscriptionPlanMapper.selectEduSubscriptionPlanById(id);
    }

    /**
     * 查询订阅计划配置列表
     * 
     * @param eduSubscriptionPlan 订阅计划配置
     * @return 订阅计划配置
     */
    @Override
    public List<EduSubscriptionPlan> selectEduSubscriptionPlanList(EduSubscriptionPlan eduSubscriptionPlan)
    {
        return eduSubscriptionPlanMapper.selectEduSubscriptionPlanList(eduSubscriptionPlan);
    }

    /**
     * 新增订阅计划配置
     * 
     * @param eduSubscriptionPlan 订阅计划配置
     * @return 结果
     */
    @Override
    public int insertEduSubscriptionPlan(EduSubscriptionPlan eduSubscriptionPlan)
    {
        eduSubscriptionPlan.setCreateTime(DateUtils.getNowDate());
        return eduSubscriptionPlanMapper.insertEduSubscriptionPlan(eduSubscriptionPlan);
    }

    /**
     * 修改订阅计划配置
     * 
     * @param eduSubscriptionPlan 订阅计划配置
     * @return 结果
     */
    @Override
    public int updateEduSubscriptionPlan(EduSubscriptionPlan eduSubscriptionPlan)
    {
        eduSubscriptionPlan.setUpdateTime(DateUtils.getNowDate());
        return eduSubscriptionPlanMapper.updateEduSubscriptionPlan(eduSubscriptionPlan);
    }

    /**
     * 批量删除订阅计划配置
     * 
     * @param ids 需要删除的订阅计划配置主键
     * @return 结果
     */
    @Override
    public int deleteEduSubscriptionPlanByIds(Long[] ids)
    {
        return eduSubscriptionPlanMapper.deleteEduSubscriptionPlanByIds(ids);
    }

    /**
     * 删除订阅计划配置信息
     * 
     * @param id 订阅计划配置主键
     * @return 结果
     */
    @Override
    public int deleteEduSubscriptionPlanById(Long id)
    {
        return eduSubscriptionPlanMapper.deleteEduSubscriptionPlanById(id);
    }
}
