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
import com.edudu.edu.domain.EduTaskRecord;
import com.edudu.edu.service.IEduTaskRecordService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 用户任务记录Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/userRecord")
public class EduTaskRecordController extends BaseController
{
    @Autowired
    private IEduTaskRecordService eduTaskRecordService;

    /**
     * 查询用户任务记录列表
     */
    @PreAuthorize("@ss.hasPermi('edu:userRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduTaskRecord eduTaskRecord)
    {
        startPage();
        List<EduTaskRecord> list = eduTaskRecordService.selectEduTaskRecordList(eduTaskRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户任务记录列表
     */
    @PreAuthorize("@ss.hasPermi('edu:userRecord:export')")
    @Log(title = "用户任务记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduTaskRecord eduTaskRecord)
    {
        List<EduTaskRecord> list = eduTaskRecordService.selectEduTaskRecordList(eduTaskRecord);
        ExcelUtil<EduTaskRecord> util = new ExcelUtil<EduTaskRecord>(EduTaskRecord.class);
        util.exportExcel(response, list, "用户任务记录数据");
    }

    /**
     * 获取用户任务记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:userRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduTaskRecordService.selectEduTaskRecordById(id));
    }

    /**
     * 新增用户任务记录
     */
    @PreAuthorize("@ss.hasPermi('edu:userRecord:add')")
    @Log(title = "用户任务记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduTaskRecord eduTaskRecord)
    {
        return toAjax(eduTaskRecordService.insertEduTaskRecord(eduTaskRecord));
    }

    /**
     * 修改用户任务记录
     */
    @PreAuthorize("@ss.hasPermi('edu:userRecord:edit')")
    @Log(title = "用户任务记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduTaskRecord eduTaskRecord)
    {
        return toAjax(eduTaskRecordService.updateEduTaskRecord(eduTaskRecord));
    }

    /**
     * 删除用户任务记录
     */
    @PreAuthorize("@ss.hasPermi('edu:userRecord:remove')")
    @Log(title = "用户任务记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduTaskRecordService.deleteEduTaskRecordByIds(ids));
    }
}
