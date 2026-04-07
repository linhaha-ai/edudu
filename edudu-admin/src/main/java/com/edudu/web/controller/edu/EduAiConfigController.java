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
import com.edudu.edu.domain.EduAiConfig;
import com.edudu.edu.service.IEduAiConfigService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * AI配置Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/aiConfig")
public class EduAiConfigController extends BaseController
{
    @Autowired
    private IEduAiConfigService eduAiConfigService;

    /**
     * 查询AI配置列表
     */
    @PreAuthorize("@ss.hasPermi('edu:aiConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduAiConfig eduAiConfig)
    {
        startPage();
        List<EduAiConfig> list = eduAiConfigService.selectEduAiConfigList(eduAiConfig);
        return getDataTable(list);
    }

    /**
     * 导出AI配置列表
     */
    @PreAuthorize("@ss.hasPermi('edu:aiConfig:export')")
    @Log(title = "AI配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduAiConfig eduAiConfig)
    {
        List<EduAiConfig> list = eduAiConfigService.selectEduAiConfigList(eduAiConfig);
        ExcelUtil<EduAiConfig> util = new ExcelUtil<EduAiConfig>(EduAiConfig.class);
        util.exportExcel(response, list, "AI配置数据");
    }

    /**
     * 获取AI配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:aiConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduAiConfigService.selectEduAiConfigById(id));
    }

    /**
     * 新增AI配置
     */
    @PreAuthorize("@ss.hasPermi('edu:aiConfig:add')")
    @Log(title = "AI配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduAiConfig eduAiConfig)
    {
        return toAjax(eduAiConfigService.insertEduAiConfig(eduAiConfig));
    }

    /**
     * 修改AI配置
     */
    @PreAuthorize("@ss.hasPermi('edu:aiConfig:edit')")
    @Log(title = "AI配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduAiConfig eduAiConfig)
    {
        return toAjax(eduAiConfigService.updateEduAiConfig(eduAiConfig));
    }

    /**
     * 删除AI配置
     */
    @PreAuthorize("@ss.hasPermi('edu:aiConfig:remove')")
    @Log(title = "AI配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduAiConfigService.deleteEduAiConfigByIds(ids));
    }
}
