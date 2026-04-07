package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduStoryRecordMapper;
import com.edudu.edu.domain.EduStoryRecord;
import com.edudu.edu.service.IEduStoryRecordService;

/**
 * 故事记录Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduStoryRecordServiceImpl implements IEduStoryRecordService 
{
    @Autowired
    private EduStoryRecordMapper eduStoryRecordMapper;

    /**
     * 查询故事记录
     * 
     * @param id 故事记录主键
     * @return 故事记录
     */
    @Override
    public EduStoryRecord selectEduStoryRecordById(Long id)
    {
        return eduStoryRecordMapper.selectEduStoryRecordById(id);
    }

    /**
     * 查询故事记录列表
     * 
     * @param eduStoryRecord 故事记录
     * @return 故事记录
     */
    @Override
    public List<EduStoryRecord> selectEduStoryRecordList(EduStoryRecord eduStoryRecord)
    {
        return eduStoryRecordMapper.selectEduStoryRecordList(eduStoryRecord);
    }

    /**
     * 新增故事记录
     * 
     * @param eduStoryRecord 故事记录
     * @return 结果
     */
    @Override
    public int insertEduStoryRecord(EduStoryRecord eduStoryRecord)
    {
        eduStoryRecord.setCreateTime(DateUtils.getNowDate());
        return eduStoryRecordMapper.insertEduStoryRecord(eduStoryRecord);
    }

    /**
     * 修改故事记录
     * 
     * @param eduStoryRecord 故事记录
     * @return 结果
     */
    @Override
    public int updateEduStoryRecord(EduStoryRecord eduStoryRecord)
    {
        eduStoryRecord.setUpdateTime(DateUtils.getNowDate());
        return eduStoryRecordMapper.updateEduStoryRecord(eduStoryRecord);
    }

    /**
     * 批量删除故事记录
     * 
     * @param ids 需要删除的故事记录主键
     * @return 结果
     */
    @Override
    public int deleteEduStoryRecordByIds(Long[] ids)
    {
        return eduStoryRecordMapper.deleteEduStoryRecordByIds(ids);
    }

    /**
     * 删除故事记录信息
     * 
     * @param id 故事记录主键
     * @return 结果
     */
    @Override
    public int deleteEduStoryRecordById(Long id)
    {
        return eduStoryRecordMapper.deleteEduStoryRecordById(id);
    }
}
