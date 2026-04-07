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
import com.edudu.edu.domain.EduTaskDaily;
import com.edudu.edu.service.IEduTaskDailyService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 每日任务分配Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/daily")
public class EduTaskDailyController extends BaseController
{
    @Autowired
    private IEduTaskDailyService eduTaskDailyService;

    /**
     * 查询每日任务分配列表
     */
    @PreAuthorize("@ss.hasPermi('edu:daily:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduTaskDaily eduTaskDaily)
    {
        startPage();
        List<EduTaskDaily> list = eduTaskDailyService.selectEduTaskDailyList(eduTaskDaily);
        return getDataTable(list);
    }

    /**
     * 导出每日任务分配列表
     */
    @PreAuthorize("@ss.hasPermi('edu:daily:export')")
    @Log(title = "每日任务分配", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduTaskDaily eduTaskDaily)
    {
        List<EduTaskDaily> list = eduTaskDailyService.selectEduTaskDailyList(eduTaskDaily);
        ExcelUtil<EduTaskDaily> util = new ExcelUtil<EduTaskDaily>(EduTaskDaily.class);
        util.exportExcel(response, list, "每日任务分配数据");
    }

    /**
     * 获取每日任务分配详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:daily:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduTaskDailyService.selectEduTaskDailyById(id));
    }

    /**
     * 新增每日任务分配
     */
    @PreAuthorize("@ss.hasPermi('edu:daily:add')")
    @Log(title = "每日任务分配", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduTaskDaily eduTaskDaily)
    {
        return toAjax(eduTaskDailyService.insertEduTaskDaily(eduTaskDaily));
    }

    /**
     * 修改每日任务分配
     */
    @PreAuthorize("@ss.hasPermi('edu:daily:edit')")
    @Log(title = "每日任务分配", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduTaskDaily eduTaskDaily)
    {
        return toAjax(eduTaskDailyService.updateEduTaskDaily(eduTaskDaily));
    }

    /**
     * 删除每日任务分配
     */
    @PreAuthorize("@ss.hasPermi('edu:daily:remove')")
    @Log(title = "每日任务分配", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduTaskDailyService.deleteEduTaskDailyByIds(ids));
    }
}
