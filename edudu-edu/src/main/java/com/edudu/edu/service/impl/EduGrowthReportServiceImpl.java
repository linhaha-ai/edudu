package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduGrowthReportMapper;
import com.edudu.edu.domain.EduGrowthReport;
import com.edudu.edu.service.IEduGrowthReportService;

/**
 * 成长报告Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduGrowthReportServiceImpl implements IEduGrowthReportService 
{
    @Autowired
    private EduGrowthReportMapper eduGrowthReportMapper;

    /**
     * 查询成长报告
     * 
     * @param id 成长报告主键
     * @return 成长报告
     */
    @Override
    public EduGrowthReport selectEduGrowthReportById(Long id)
    {
        return eduGrowthReportMapper.selectEduGrowthReportById(id);
    }

    /**
     * 查询成长报告列表
     * 
     * @param eduGrowthReport 成长报告
     * @return 成长报告
     */
    @Override
    public List<EduGrowthReport> selectEduGrowthReportList(EduGrowthReport eduGrowthReport)
    {
        return eduGrowthReportMapper.selectEduGrowthReportList(eduGrowthReport);
    }

    /**
     * 新增成长报告
     * 
     * @param eduGrowthReport 成长报告
     * @return 结果
     */
    @Override
    public int insertEduGrowthReport(EduGrowthReport eduGrowthReport)
    {
        eduGrowthReport.setCreateTime(DateUtils.getNowDate());
        return eduGrowthReportMapper.insertEduGrowthReport(eduGrowthReport);
    }

    /**
     * 修改成长报告
     * 
     * @param eduGrowthReport 成长报告
     * @return 结果
     */
    @Override
    public int updateEduGrowthReport(EduGrowthReport eduGrowthReport)
    {
        return eduGrowthReportMapper.updateEduGrowthReport(eduGrowthReport);
    }

    /**
     * 批量删除成长报告
     * 
     * @param ids 需要删除的成长报告主键
     * @return 结果
     */
    @Override
    public int deleteEduGrowthReportByIds(Long[] ids)
    {
        return eduGrowthReportMapper.deleteEduGrowthReportByIds(ids);
    }

    /**
     * 删除成长报告信息
     * 
     * @param id 成长报告主键
     * @return 结果
     */
    @Override
    public int deleteEduGrowthReportById(Long id) {
        return eduGrowthReportMapper.deleteEduGrowthReportById(id);
    }

    /**
     * 根据孩子ID查询成长报告列表
     */
    @Override
    public List<EduGrowthReport> selectEduGrowthReportListByChildId(Long childId) {
        return eduGrowthReportMapper.selectEduGrowthReportListByChildId(childId);
    }
}
