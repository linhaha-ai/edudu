package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.edu.domain.EduDailyTask;

/**
 * 每日任务模板Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduDailyTaskMapper 
{
    /**
     * 查询每日任务模板
     * 
     * @param id 每日任务模板主键
     * @return 每日任务模板
     */
    public EduDailyTask selectEduDailyTaskById(Long id);

    /**
     * 查询每日任务模板列表
     * 
     * @param eduDailyTask 每日任务模板
     * @return 每日任务模板集合
     */
    public List<EduDailyTask> selectEduDailyTaskList(EduDailyTask eduDailyTask);

    /**
     * 新增每日任务模板
     * 
     * @param eduDailyTask 每日任务模板
     * @return 结果
     */
    public int insertEduDailyTask(EduDailyTask eduDailyTask);

    /**
     * 修改每日任务模板
     * 
     * @param eduDailyTask 每日任务模板
     * @return 结果
     */
    public int updateEduDailyTask(EduDailyTask eduDailyTask);

    /**
     * 删除每日任务模板
     * 
     * @param id 每日任务模板主键
     * @return 结果
     */
    public int deleteEduDailyTaskById(Long id);

    /**
     * 批量删除每日任务模板
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduDailyTaskByIds(Long[] ids);
}
