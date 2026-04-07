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
import com.edudu.edu.domain.EduDictGrowthType;
import com.edudu.edu.service.IEduDictGrowthTypeService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 成长记录类型字典Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/type")
public class EduDictGrowthTypeController extends BaseController
{
    @Autowired
    private IEduDictGrowthTypeService eduDictGrowthTypeService;

    /**
     * 查询成长记录类型字典列表
     */
    @PreAuthorize("@ss.hasPermi('edu:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduDictGrowthType eduDictGrowthType)
    {
        startPage();
        List<EduDictGrowthType> list = eduDictGrowthTypeService.selectEduDictGrowthTypeList(eduDictGrowthType);
        return getDataTable(list);
    }

    /**
     * 导出成长记录类型字典列表
     */
    @PreAuthorize("@ss.hasPermi('edu:type:export')")
    @Log(title = "成长记录类型字典", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduDictGrowthType eduDictGrowthType)
    {
        List<EduDictGrowthType> list = eduDictGrowthTypeService.selectEduDictGrowthTypeList(eduDictGrowthType);
        ExcelUtil<EduDictGrowthType> util = new ExcelUtil<EduDictGrowthType>(EduDictGrowthType.class);
        util.exportExcel(response, list, "成长记录类型字典数据");
    }

    /**
     * 获取成长记录类型字典详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduDictGrowthTypeService.selectEduDictGrowthTypeById(id));
    }

    /**
     * 新增成长记录类型字典
     */
    @PreAuthorize("@ss.hasPermi('edu:type:add')")
    @Log(title = "成长记录类型字典", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduDictGrowthType eduDictGrowthType)
    {
        return toAjax(eduDictGrowthTypeService.insertEduDictGrowthType(eduDictGrowthType));
    }

    /**
     * 修改成长记录类型字典
     */
    @PreAuthorize("@ss.hasPermi('edu:type:edit')")
    @Log(title = "成长记录类型字典", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduDictGrowthType eduDictGrowthType)
    {
        return toAjax(eduDictGrowthTypeService.updateEduDictGrowthType(eduDictGrowthType));
    }

    /**
     * 删除成长记录类型字典
     */
    @PreAuthorize("@ss.hasPermi('edu:type:remove')")
    @Log(title = "成长记录类型字典", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduDictGrowthTypeService.deleteEduDictGrowthTypeByIds(ids));
    }
}
