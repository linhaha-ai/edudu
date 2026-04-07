package com.edudu.edu.service;

import java.util.List;
import com.edudu.edu.domain.EduGrowthRecord;

/**
 * 成长记录Service接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface IEduGrowthRecordService 
{
    /**
     * 查询成长记录
     * 
     * @param id 成长记录主键
     * @return 成长记录
     */
    public EduGrowthRecord selectEduGrowthRecordById(Long id);

    /**
     * 查询成长记录列表
     * 
     * @param eduGrowthRecord 成长记录
     * @return 成长记录集合
     */
    public List<EduGrowthRecord> selectEduGrowthRecordList(EduGrowthRecord eduGrowthRecord);

    /**
     * 新增成长记录
     * 
     * @param eduGrowthRecord 成长记录
     * @return 结果
     */
    public int insertEduGrowthRecord(EduGrowthRecord eduGrowthRecord);

    /**
     * 修改成长记录
     * 
     * @param eduGrowthRecord 成长记录
     * @return 结果
     */
    public int updateEduGrowthRecord(EduGrowthRecord eduGrowthRecord);

    /**
     * 批量删除成长记录
     * 
     * @param ids 需要删除的成长记录主键集合
     * @return 结果
     */
    public int deleteEduGrowthRecordByIds(Long[] ids);

    /**
     * 删除成长记录信息
     * 
     * @param id 成长记录主键
     * @return 结果
     */
    public int deleteEduGrowthRecordById(Long id);
}
