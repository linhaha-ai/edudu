package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.edu.domain.EduTaskRecord;

/**
 * 用户任务记录Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduTaskRecordMapper 
{
    /**
     * 查询用户任务记录
     * 
     * @param id 用户任务记录主键
     * @return 用户任务记录
     */
    public EduTaskRecord selectEduTaskRecordById(Long id);

    /**
     * 查询用户任务记录列表
     * 
     * @param eduTaskRecord 用户任务记录
     * @return 用户任务记录集合
     */
    public List<EduTaskRecord> selectEduTaskRecordList(EduTaskRecord eduTaskRecord);

    /**
     * 新增用户任务记录
     * 
     * @param eduTaskRecord 用户任务记录
     * @return 结果
     */
    public int insertEduTaskRecord(EduTaskRecord eduTaskRecord);

    /**
     * 修改用户任务记录
     * 
     * @param eduTaskRecord 用户任务记录
     * @return 结果
     */
    public int updateEduTaskRecord(EduTaskRecord eduTaskRecord);

    /**
     * 删除用户任务记录
     * 
     * @param id 用户任务记录主键
     * @return 结果
     */
    public int deleteEduTaskRecordById(Long id);

    /**
     * 批量删除用户任务记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduTaskRecordByIds(Long[] ids);
}
