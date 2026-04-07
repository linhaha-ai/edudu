package com.edudu.edu.service;

import java.util.List;
import com.edudu.edu.domain.EduStoryRecord;

/**
 * 故事记录Service接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface IEduStoryRecordService 
{
    /**
     * 查询故事记录
     * 
     * @param id 故事记录主键
     * @return 故事记录
     */
    public EduStoryRecord selectEduStoryRecordById(Long id);

    /**
     * 查询故事记录列表
     * 
     * @param eduStoryRecord 故事记录
     * @return 故事记录集合
     */
    public List<EduStoryRecord> selectEduStoryRecordList(EduStoryRecord eduStoryRecord);

    /**
     * 新增故事记录
     * 
     * @param eduStoryRecord 故事记录
     * @return 结果
     */
    public int insertEduStoryRecord(EduStoryRecord eduStoryRecord);

    /**
     * 修改故事记录
     * 
     * @param eduStoryRecord 故事记录
     * @return 结果
     */
    public int updateEduStoryRecord(EduStoryRecord eduStoryRecord);

    /**
     * 批量删除故事记录
     * 
     * @param ids 需要删除的故事记录主键集合
     * @return 结果
     */
    public int deleteEduStoryRecordByIds(Long[] ids);

    /**
     * 删除故事记录信息
     * 
     * @param id 故事记录主键
     * @return 结果
     */
    public int deleteEduStoryRecordById(Long id);
}
