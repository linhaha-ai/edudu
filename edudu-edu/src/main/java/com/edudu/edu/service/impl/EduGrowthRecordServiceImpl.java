package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduGrowthRecordMapper;
import com.edudu.edu.domain.EduGrowthRecord;
import com.edudu.edu.service.IEduGrowthRecordService;

/**
 * 成长记录Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduGrowthRecordServiceImpl implements IEduGrowthRecordService 
{
    @Autowired
    private EduGrowthRecordMapper eduGrowthRecordMapper;

    /**
     * 查询成长记录
     * 
     * @param id 成长记录主键
     * @return 成长记录
     */
    @Override
    public EduGrowthRecord selectEduGrowthRecordById(Long id)
    {
        return eduGrowthRecordMapper.selectEduGrowthRecordById(id);
    }

    /**
     * 查询成长记录列表
     * 
     * @param eduGrowthRecord 成长记录
     * @return 成长记录
     */
    @Override
    public List<EduGrowthRecord> selectEduGrowthRecordList(EduGrowthRecord eduGrowthRecord)
    {
        return eduGrowthRecordMapper.selectEduGrowthRecordList(eduGrowthRecord);
    }

    /**
     * 新增成长记录
     * 
     * @param eduGrowthRecord 成长记录
     * @return 结果
     */
    @Override
    public int insertEduGrowthRecord(EduGrowthRecord eduGrowthRecord)
    {
        eduGrowthRecord.setCreateTime(DateUtils.getNowDate());
        return eduGrowthRecordMapper.insertEduGrowthRecord(eduGrowthRecord);
    }

    /**
     * 修改成长记录
     * 
     * @param eduGrowthRecord 成长记录
     * @return 结果
     */
    @Override
    public int updateEduGrowthRecord(EduGrowthRecord eduGrowthRecord)
    {
        eduGrowthRecord.setUpdateTime(DateUtils.getNowDate());
        return eduGrowthRecordMapper.updateEduGrowthRecord(eduGrowthRecord);
    }

    /**
     * 批量删除成长记录
     * 
     * @param ids 需要删除的成长记录主键
     * @return 结果
     */
    @Override
    public int deleteEduGrowthRecordByIds(Long[] ids)
    {
        return eduGrowthRecordMapper.deleteEduGrowthRecordByIds(ids);
    }

    /**
     * 删除成长记录信息
     * 
     * @param id 成长记录主键
     * @return 结果
     */
    @Override
    public int deleteEduGrowthRecordById(Long id)
    {
        return eduGrowthRecordMapper.deleteEduGrowthRecordById(id);
    }
}
