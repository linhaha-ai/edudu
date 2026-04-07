package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.edu.domain.EduSubscriptionPlan;

/**
 * 订阅计划配置Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduSubscriptionPlanMapper 
{
    /**
     * 查询订阅计划配置
     * 
     * @param id 订阅计划配置主键
     * @return 订阅计划配置
     */
    public EduSubscriptionPlan selectEduSubscriptionPlanById(Long id);

    /**
     * 查询订阅计划配置列表
     * 
     * @param eduSubscriptionPlan 订阅计划配置
     * @return 订阅计划配置集合
     */
    public List<EduSubscriptionPlan> selectEduSubscriptionPlanList(EduSubscriptionPlan eduSubscriptionPlan);

    /**
     * 新增订阅计划配置
     * 
     * @param eduSubscriptionPlan 订阅计划配置
     * @return 结果
     */
    public int insertEduSubscriptionPlan(EduSubscriptionPlan eduSubscriptionPlan);

    /**
     * 修改订阅计划配置
     * 
     * @param eduSubscriptionPlan 订阅计划配置
     * @return 结果
     */
    public int updateEduSubscriptionPlan(EduSubscriptionPlan eduSubscriptionPlan);

    /**
     * 删除订阅计划配置
     * 
     * @param id 订阅计划配置主键
     * @return 结果
     */
    public int deleteEduSubscriptionPlanById(Long id);

    /**
     * 批量删除订阅计划配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduSubscriptionPlanByIds(Long[] ids);
}
