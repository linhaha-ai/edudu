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
import com.edudu.edu.domain.EduDailyTask;
import com.edudu.edu.service.IEduDailyTaskService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 每日任务模板Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/task")
public class EduDailyTaskController extends BaseController
{
    @Autowired
    private IEduDailyTaskService eduDailyTaskService;

    /**
     * 查询每日任务模板列表
     */
    @PreAuthorize("@ss.hasPermi('edu:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduDailyTask eduDailyTask)
    {
        startPage();
        List<EduDailyTask> list = eduDailyTaskService.selectEduDailyTaskList(eduDailyTask);
        return getDataTable(list);
    }

    /**
     * 导出每日任务模板列表
     */
    @PreAuthorize("@ss.hasPermi('edu:task:export')")
    @Log(title = "每日任务模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduDailyTask eduDailyTask)
    {
        List<EduDailyTask> list = eduDailyTaskService.selectEduDailyTaskList(eduDailyTask);
        ExcelUtil<EduDailyTask> util = new ExcelUtil<EduDailyTask>(EduDailyTask.class);
        util.exportExcel(response, list, "每日任务模板数据");
    }

    /**
     * 获取每日任务模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduDailyTaskService.selectEduDailyTaskById(id));
    }

    /**
     * 新增每日任务模板
     */
    @PreAuthorize("@ss.hasPermi('edu:task:add')")
    @Log(title = "每日任务模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduDailyTask eduDailyTask)
    {
        return toAjax(eduDailyTaskService.insertEduDailyTask(eduDailyTask));
    }

    /**
     * 修改每日任务模板
     */
    @PreAuthorize("@ss.hasPermi('edu:task:edit')")
    @Log(title = "每日任务模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduDailyTask eduDailyTask)
    {
        return toAjax(eduDailyTaskService.updateEduDailyTask(eduDailyTask));
    }

    /**
     * 删除每日任务模板
     */
    @PreAuthorize("@ss.hasPermi('edu:task:remove')")
    @Log(title = "每日任务模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduDailyTaskService.deleteEduDailyTaskByIds(ids));
    }
}
