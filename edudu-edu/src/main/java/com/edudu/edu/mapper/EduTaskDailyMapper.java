package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.edu.domain.EduTaskDaily;

/**
 * 每日任务分配Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduTaskDailyMapper 
{
    /**
     * 查询每日任务分配
     * 
     * @param id 每日任务分配主键
     * @return 每日任务分配
     */
    public EduTaskDaily selectEduTaskDailyById(Long id);

    /**
     * 查询每日任务分配列表
     * 
     * @param eduTaskDaily 每日任务分配
     * @return 每日任务分配集合
     */
    public List<EduTaskDaily> selectEduTaskDailyList(EduTaskDaily eduTaskDaily);

    /**
     * 新增每日任务分配
     * 
     * @param eduTaskDaily 每日任务分配
     * @return 结果
     */
    public int insertEduTaskDaily(EduTaskDaily eduTaskDaily);

    /**
     * 修改每日任务分配
     * 
     * @param eduTaskDaily 每日任务分配
     * @return 结果
     */
    public int updateEduTaskDaily(EduTaskDaily eduTaskDaily);

    /**
     * 删除每日任务分配
     * 
     * @param id 每日任务分配主键
     * @return 结果
     */
    public int deleteEduTaskDailyById(Long id);

    /**
     * 批量删除每日任务分配
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduTaskDailyByIds(Long[] ids);
}
