package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduTaskRecordMapper;
import com.edudu.edu.domain.EduTaskRecord;
import com.edudu.edu.service.IEduTaskRecordService;

/**
 * 用户任务记录Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduTaskRecordServiceImpl implements IEduTaskRecordService 
{
    @Autowired
    private EduTaskRecordMapper eduTaskRecordMapper;

    /**
     * 查询用户任务记录
     * 
     * @param id 用户任务记录主键
     * @return 用户任务记录
     */
    @Override
    public EduTaskRecord selectEduTaskRecordById(Long id)
    {
        return eduTaskRecordMapper.selectEduTaskRecordById(id);
    }

    /**
     * 查询用户任务记录列表
     * 
     * @param eduTaskRecord 用户任务记录
     * @return 用户任务记录
     */
    @Override
    public List<EduTaskRecord> selectEduTaskRecordList(EduTaskRecord eduTaskRecord)
    {
        return eduTaskRecordMapper.selectEduTaskRecordList(eduTaskRecord);
    }

    /**
     * 新增用户任务记录
     * 
     * @param eduTaskRecord 用户任务记录
     * @return 结果
     */
    @Override
    public int insertEduTaskRecord(EduTaskRecord eduTaskRecord)
    {
        eduTaskRecord.setCreateTime(DateUtils.getNowDate());
        return eduTaskRecordMapper.insertEduTaskRecord(eduTaskRecord);
    }

    /**
     * 修改用户任务记录
     * 
     * @param eduTaskRecord 用户任务记录
     * @return 结果
     */
    @Override
    public int updateEduTaskRecord(EduTaskRecord eduTaskRecord)
    {
        eduTaskRecord.setUpdateTime(DateUtils.getNowDate());
        return eduTaskRecordMapper.updateEduTaskRecord(eduTaskRecord);
    }

    /**
     * 批量删除用户任务记录
     * 
     * @param ids 需要删除的用户任务记录主键
     * @return 结果
     */
    @Override
    public int deleteEduTaskRecordByIds(Long[] ids)
    {
        return eduTaskRecordMapper.deleteEduTaskRecordByIds(ids);
    }

    /**
     * 删除用户任务记录信息
     * 
     * @param id 用户任务记录主键
     * @return 结果
     */
    @Override
    public int deleteEduTaskRecordById(Long id)
    {
        return eduTaskRecordMapper.deleteEduTaskRecordById(id);
    }
}
