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
import com.edudu.edu.domain.EduStoryRecord;
import com.edudu.edu.service.IEduStoryRecordService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 故事记录Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/storeRecord")
public class EduStoryRecordController extends BaseController
{
    @Autowired
    private IEduStoryRecordService eduStoryRecordService;

    /**
     * 查询故事记录列表
     */
    @PreAuthorize("@ss.hasPermi('edu:storeRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduStoryRecord eduStoryRecord)
    {
        startPage();
        List<EduStoryRecord> list = eduStoryRecordService.selectEduStoryRecordList(eduStoryRecord);
        return getDataTable(list);
    }

    /**
     * 导出故事记录列表
     */
    @PreAuthorize("@ss.hasPermi('edu:storeRecord:export')")
    @Log(title = "故事记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduStoryRecord eduStoryRecord)
    {
        List<EduStoryRecord> list = eduStoryRecordService.selectEduStoryRecordList(eduStoryRecord);
        ExcelUtil<EduStoryRecord> util = new ExcelUtil<EduStoryRecord>(EduStoryRecord.class);
        util.exportExcel(response, list, "故事记录数据");
    }

    /**
     * 获取故事记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:storeRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduStoryRecordService.selectEduStoryRecordById(id));
    }

    /**
     * 新增故事记录
     */
    @PreAuthorize("@ss.hasPermi('edu:storeRecord:add')")
    @Log(title = "故事记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduStoryRecord eduStoryRecord)
    {
        return toAjax(eduStoryRecordService.insertEduStoryRecord(eduStoryRecord));
    }

    /**
     * 修改故事记录
     */
    @PreAuthorize("@ss.hasPermi('edu:storeRecord:edit')")
    @Log(title = "故事记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduStoryRecord eduStoryRecord)
    {
        return toAjax(eduStoryRecordService.updateEduStoryRecord(eduStoryRecord));
    }

    /**
     * 删除故事记录
     */
    @PreAuthorize("@ss.hasPermi('edu:storeRecord:remove')")
    @Log(title = "故事记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduStoryRecordService.deleteEduStoryRecordByIds(ids));
    }
}
