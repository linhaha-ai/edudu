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
import com.edudu.edu.domain.EduChild;
import com.edudu.edu.service.IEduChildService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 孩子信息Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/child")
public class EduChildController extends BaseController
{
    @Autowired
    private IEduChildService eduChildService;

    /**
     * 查询孩子信息列表
     */
    @PreAuthorize("@ss.hasPermi('edu:child:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduChild eduChild)
    {
        startPage();
        List<EduChild> list = eduChildService.selectEduChildList(eduChild);
        return getDataTable(list);
    }

    /**
     * 导出孩子信息列表
     */
    @PreAuthorize("@ss.hasPermi('edu:child:export')")
    @Log(title = "孩子信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduChild eduChild)
    {
        List<EduChild> list = eduChildService.selectEduChildList(eduChild);
        ExcelUtil<EduChild> util = new ExcelUtil<EduChild>(EduChild.class);
        util.exportExcel(response, list, "孩子信息数据");
    }

    /**
     * 获取孩子信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:child:query')")
    @GetMapping(value = "/{childId}")
    public AjaxResult getInfo(@PathVariable("childId") Long childId)
    {
        return success(eduChildService.selectEduChildByChildId(childId));
    }

    /**
     * 新增孩子信息
     */
    @PreAuthorize("@ss.hasPermi('edu:child:add')")
    @Log(title = "孩子信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduChild eduChild)
    {
        return toAjax(eduChildService.insertEduChild(eduChild));
    }

    /**
     * 修改孩子信息
     */
    @PreAuthorize("@ss.hasPermi('edu:child:edit')")
    @Log(title = "孩子信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduChild eduChild)
    {
        return toAjax(eduChildService.updateEduChild(eduChild));
    }

    /**
     * 删除孩子信息
     */
    @PreAuthorize("@ss.hasPermi('edu:child:remove')")
    @Log(title = "孩子信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{childIds}")
    public AjaxResult remove(@PathVariable Long[] childIds)
    {
        return toAjax(eduChildService.deleteEduChildByChildIds(childIds));
    }
}
