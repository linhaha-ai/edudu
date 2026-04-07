package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.edu.domain.EduDictGoal;

/**
 * 教育目标字典Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduDictGoalMapper 
{
    /**
     * 查询教育目标字典
     * 
     * @param id 教育目标字典主键
     * @return 教育目标字典
     */
    public EduDictGoal selectEduDictGoalById(Long id);

    /**
     * 查询教育目标字典列表
     * 
     * @param eduDictGoal 教育目标字典
     * @return 教育目标字典集合
     */
    public List<EduDictGoal> selectEduDictGoalList(EduDictGoal eduDictGoal);

    /**
     * 新增教育目标字典
     * 
     * @param eduDictGoal 教育目标字典
     * @return 结果
     */
    public int insertEduDictGoal(EduDictGoal eduDictGoal);

    /**
     * 修改教育目标字典
     * 
     * @param eduDictGoal 教育目标字典
     * @return 结果
     */
    public int updateEduDictGoal(EduDictGoal eduDictGoal);

    /**
     * 删除教育目标字典
     * 
     * @param id 教育目标字典主键
     * @return 结果
     */
    public int deleteEduDictGoalById(Long id);

    /**
     * 批量删除教育目标字典
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduDictGoalByIds(Long[] ids);
}
