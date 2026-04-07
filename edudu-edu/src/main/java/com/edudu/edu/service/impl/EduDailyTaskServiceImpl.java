package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduDailyTaskMapper;
import com.edudu.edu.domain.EduDailyTask;
import com.edudu.edu.service.IEduDailyTaskService;

/**
 * 每日任务模板Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduDailyTaskServiceImpl implements IEduDailyTaskService 
{
    @Autowired
    private EduDailyTaskMapper eduDailyTaskMapper;

    /**
     * 查询每日任务模板
     * 
     * @param id 每日任务模板主键
     * @return 每日任务模板
     */
    @Override
    public EduDailyTask selectEduDailyTaskById(Long id)
    {
        return eduDailyTaskMapper.selectEduDailyTaskById(id);
    }

    /**
     * 查询每日任务模板列表
     * 
     * @param eduDailyTask 每日任务模板
     * @return 每日任务模板
     */
    @Override
    public List<EduDailyTask> selectEduDailyTaskList(EduDailyTask eduDailyTask)
    {
        return eduDailyTaskMapper.selectEduDailyTaskList(eduDailyTask);
    }

    /**
     * 新增每日任务模板
     * 
     * @param eduDailyTask 每日任务模板
     * @return 结果
     */
    @Override
    public int insertEduDailyTask(EduDailyTask eduDailyTask)
    {
        eduDailyTask.setCreateTime(DateUtils.getNowDate());
        return eduDailyTaskMapper.insertEduDailyTask(eduDailyTask);
    }

    /**
     * 修改每日任务模板
     * 
     * @param eduDailyTask 每日任务模板
     * @return 结果
     */
    @Override
    public int updateEduDailyTask(EduDailyTask eduDailyTask)
    {
        eduDailyTask.setUpdateTime(DateUtils.getNowDate());
        return eduDailyTaskMapper.updateEduDailyTask(eduDailyTask);
    }

    /**
     * 批量删除每日任务模板
     * 
     * @param ids 需要删除的每日任务模板主键
     * @return 结果
     */
    @Override
    public int deleteEduDailyTaskByIds(Long[] ids)
    {
        return eduDailyTaskMapper.deleteEduDailyTaskByIds(ids);
    }

    /**
     * 删除每日任务模板信息
     * 
     * @param id 每日任务模板主键
     * @return 结果
     */
    @Override
    public int deleteEduDailyTaskById(Long id)
    {
        return eduDailyTaskMapper.deleteEduDailyTaskById(id);
    }
}
