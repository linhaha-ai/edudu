package com.edudu.web.controller.edu;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.edudu.common.annotation.Log;
import com.edudu.common.core.controller.BaseController;
import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.enums.BusinessType;
import com.edudu.edu.domain.EduGrowthReport;
import com.edudu.edu.service.IEduGrowthReportService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 成长报告Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/growthReport")
public class EduGrowthReportController extends BaseController
{
    @Autowired
    private IEduGrowthReportService eduGrowthReportService;

    /**
     * 查询成长报告列表
     */
    @PreAuthorize("@ss.hasPermi('edu:growthReport:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduGrowthReport eduGrowthReport)
    {
        startPage();
        List<EduGrowthReport> list = eduGrowthReportService.selectEduGrowthReportList(eduGrowthReport);
        return getDataTable(list);
    }

    /**
     * 导出成长报告列表
     */
    @PreAuthorize("@ss.hasPermi('edu:growthReport:export')")
    @Log(title = "成长报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduGrowthReport eduGrowthReport)
    {
        List<EduGrowthReport> list = eduGrowthReportService.selectEduGrowthReportList(eduGrowthReport);
        ExcelUtil<EduGrowthReport> util = new ExcelUtil<EduGrowthReport>(EduGrowthReport.class);
        util.exportExcel(response, list, "成长报告数据");
    }

    /**
     * 获取成长报告详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:growthReport:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduGrowthReportService.selectEduGrowthReportById(id));
    }

    /**
     * 新增成长报告
     */
    @PreAuthorize("@ss.hasPermi('edu:growthReport:add')")
    @Log(title = "成长报告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduGrowthReport eduGrowthReport)
    {
        return toAjax(eduGrowthReportService.insertEduGrowthReport(eduGrowthReport));
    }

    /**
     * 修改成长报告
     */
    @PreAuthorize("@ss.hasPermi('edu:growthReport:edit')")
    @Log(title = "成长报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduGrowthReport eduGrowthReport)
    {
        return toAjax(eduGrowthReportService.updateEduGrowthReport(eduGrowthReport));
    }

    /**
     * 删除成长报告
     */
    @PreAuthorize("@ss.hasPermi('edu:growthReport:remove')")
    @Log(title = "成长报告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduGrowthReportService.deleteEduGrowthReportByIds(ids));
    }
}
