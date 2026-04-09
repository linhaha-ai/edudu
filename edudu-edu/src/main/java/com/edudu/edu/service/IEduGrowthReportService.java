package com.edudu.edu.service;

import java.util.List;
import com.edudu.edu.domain.EduGrowthReport;

/**
 * 成长报告Service接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface IEduGrowthReportService 
{
    /**
     * 查询成长报告
     * 
     * @param id 成长报告主键
     * @return 成长报告
     */
    public EduGrowthReport selectEduGrowthReportById(Long id);

    /**
     * 查询成长报告列表
     * 
     * @param eduGrowthReport 成长报告
     * @return 成长报告集合
     */
    public List<EduGrowthReport> selectEduGrowthReportList(EduGrowthReport eduGrowthReport);

    /**
     * 新增成长报告
     * 
     * @param eduGrowthReport 成长报告
     * @return 结果
     */
    public int insertEduGrowthReport(EduGrowthReport eduGrowthReport);

    /**
     * 修改成长报告
     * 
     * @param eduGrowthReport 成长报告
     * @return 结果
     */
    public int updateEduGrowthReport(EduGrowthReport eduGrowthReport);

    /**
     * 批量删除成长报告
     * 
     * @param ids 需要删除的成长报告主键集合
     * @return 结果
     */
    public int deleteEduGrowthReportByIds(Long[] ids);

    /**
     * 删除成长报告信息
     * 
     * @param id 成长报告主键
     * @return 结果
     */
    public int deleteEduGrowthReportById(Long id);

    /**
     * 根据孩子ID查询成长报告列表
     * 
     * @param childId 孩子ID
     * @return 成长报告列表
     */
    public List<EduGrowthReport> selectEduGrowthReportListByChildId(Long childId);
}
