package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduDictGoalMapper;
import com.edudu.edu.domain.EduDictGoal;
import com.edudu.edu.service.IEduDictGoalService;

/**
 * 教育目标字典Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduDictGoalServiceImpl implements IEduDictGoalService 
{
    @Autowired
    private EduDictGoalMapper eduDictGoalMapper;

    /**
     * 查询教育目标字典
     * 
     * @param id 教育目标字典主键
     * @return 教育目标字典
     */
    @Override
    public EduDictGoal selectEduDictGoalById(Long id)
    {
        return eduDictGoalMapper.selectEduDictGoalById(id);
    }

    /**
     * 查询教育目标字典列表
     * 
     * @param eduDictGoal 教育目标字典
     * @return 教育目标字典
     */
    @Override
    public List<EduDictGoal> selectEduDictGoalList(EduDictGoal eduDictGoal)
    {
        return eduDictGoalMapper.selectEduDictGoalList(eduDictGoal);
    }

    /**
     * 新增教育目标字典
     * 
     * @param eduDictGoal 教育目标字典
     * @return 结果
     */
    @Override
    public int insertEduDictGoal(EduDictGoal eduDictGoal)
    {
        eduDictGoal.setCreateTime(DateUtils.getNowDate());
        return eduDictGoalMapper.insertEduDictGoal(eduDictGoal);
    }

    /**
     * 修改教育目标字典
     * 
     * @param eduDictGoal 教育目标字典
     * @return 结果
     */
    @Override
    public int updateEduDictGoal(EduDictGoal eduDictGoal)
    {
        eduDictGoal.setUpdateTime(DateUtils.getNowDate());
        return eduDictGoalMapper.updateEduDictGoal(eduDictGoal);
    }

    /**
     * 批量删除教育目标字典
     * 
     * @param ids 需要删除的教育目标字典主键
     * @return 结果
     */
    @Override
    public int deleteEduDictGoalByIds(Long[] ids)
    {
        return eduDictGoalMapper.deleteEduDictGoalByIds(ids);
    }

    /**
     * 删除教育目标字典信息
     * 
     * @param id 教育目标字典主键
     * @return 结果
     */
    @Override
    public int deleteEduDictGoalById(Long id)
    {
        return eduDictGoalMapper.deleteEduDictGoalById(id);
    }
}
