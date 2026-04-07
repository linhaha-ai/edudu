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
import com.edudu.edu.domain.EduGrowthRecord;
import com.edudu.edu.service.IEduGrowthRecordService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 成长记录Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/growthRecord")
public class EduGrowthRecordController extends BaseController
{
    @Autowired
    private IEduGrowthRecordService eduGrowthRecordService;

    /**
     * 查询成长记录列表
     */
    @PreAuthorize("@ss.hasPermi('edu:growthRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduGrowthRecord eduGrowthRecord)
    {
        startPage();
        List<EduGrowthRecord> list = eduGrowthRecordService.selectEduGrowthRecordList(eduGrowthRecord);
        return getDataTable(list);
    }

    /**
     * 导出成长记录列表
     */
    @PreAuthorize("@ss.hasPermi('edu:growthRecord:export')")
    @Log(title = "成长记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduGrowthRecord eduGrowthRecord)
    {
        List<EduGrowthRecord> list = eduGrowthRecordService.selectEduGrowthRecordList(eduGrowthRecord);
        ExcelUtil<EduGrowthRecord> util = new ExcelUtil<EduGrowthRecord>(EduGrowthRecord.class);
        util.exportExcel(response, list, "成长记录数据");
    }

    /**
     * 获取成长记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:growthRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduGrowthRecordService.selectEduGrowthRecordById(id));
    }

    /**
     * 新增成长记录
     */
    @PreAuthorize("@ss.hasPermi('edu:growthRecord:add')")
    @Log(title = "成长记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduGrowthRecord eduGrowthRecord)
    {
        return toAjax(eduGrowthRecordService.insertEduGrowthRecord(eduGrowthRecord));
    }

    /**
     * 修改成长记录
     */
    @PreAuthorize("@ss.hasPermi('edu:growthRecord:edit')")
    @Log(title = "成长记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduGrowthRecord eduGrowthRecord)
    {
        return toAjax(eduGrowthRecordService.updateEduGrowthRecord(eduGrowthRecord));
    }

    /**
     * 删除成长记录
     */
    @PreAuthorize("@ss.hasPermi('edu:growthRecord:remove')")
    @Log(title = "成长记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduGrowthRecordService.deleteEduGrowthRecordByIds(ids));
    }
}
