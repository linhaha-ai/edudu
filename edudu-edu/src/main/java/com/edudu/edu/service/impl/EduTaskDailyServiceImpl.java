package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduTaskDailyMapper;
import com.edudu.edu.domain.EduTaskDaily;
import com.edudu.edu.service.IEduTaskDailyService;

/**
 * 每日任务分配Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduTaskDailyServiceImpl implements IEduTaskDailyService 
{
    @Autowired
    private EduTaskDailyMapper eduTaskDailyMapper;

    /**
     * 查询每日任务分配
     * 
     * @param id 每日任务分配主键
     * @return 每日任务分配
     */
    @Override
    public EduTaskDaily selectEduTaskDailyById(Long id)
    {
        return eduTaskDailyMapper.selectEduTaskDailyById(id);
    }

    /**
     * 查询每日任务分配列表
     * 
     * @param eduTaskDaily 每日任务分配
     * @return 每日任务分配
     */
    @Override
    public List<EduTaskDaily> selectEduTaskDailyList(EduTaskDaily eduTaskDaily)
    {
        return eduTaskDailyMapper.selectEduTaskDailyList(eduTaskDaily);
    }

    /**
     * 新增每日任务分配
     * 
     * @param eduTaskDaily 每日任务分配
     * @return 结果
     */
    @Override
    public int insertEduTaskDaily(EduTaskDaily eduTaskDaily)
    {
        eduTaskDaily.setCreateTime(DateUtils.getNowDate());
        return eduTaskDailyMapper.insertEduTaskDaily(eduTaskDaily);
    }

    /**
     * 修改每日任务分配
     * 
     * @param eduTaskDaily 每日任务分配
     * @return 结果
     */
    @Override
    public int updateEduTaskDaily(EduTaskDaily eduTaskDaily)
    {
        eduTaskDaily.setUpdateTime(DateUtils.getNowDate());
        return eduTaskDailyMapper.updateEduTaskDaily(eduTaskDaily);
    }

    /**
     * 批量删除每日任务分配
     * 
     * @param ids 需要删除的每日任务分配主键
     * @return 结果
     */
    @Override
    public int deleteEduTaskDailyByIds(Long[] ids)
    {
        return eduTaskDailyMapper.deleteEduTaskDailyByIds(ids);
    }

    /**
     * 删除每日任务分配信息
     * 
     * @param id 每日任务分配主键
     * @return 结果
     */
    @Override
    public int deleteEduTaskDailyById(Long id)
    {
        return eduTaskDailyMapper.deleteEduTaskDailyById(id);
    }
}
