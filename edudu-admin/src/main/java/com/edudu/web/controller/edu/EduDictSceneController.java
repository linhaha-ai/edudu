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
import com.edudu.edu.domain.EduDictScene;
import com.edudu.edu.service.IEduDictSceneService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 故事场景字典Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/scene")
public class EduDictSceneController extends BaseController
{
    @Autowired
    private IEduDictSceneService eduDictSceneService;

    /**
     * 查询故事场景字典列表
     */
    @PreAuthorize("@ss.hasPermi('edu:scene:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduDictScene eduDictScene)
    {
        startPage();
        List<EduDictScene> list = eduDictSceneService.selectEduDictSceneList(eduDictScene);
        return getDataTable(list);
    }

    /**
     * 导出故事场景字典列表
     */
    @PreAuthorize("@ss.hasPermi('edu:scene:export')")
    @Log(title = "故事场景字典", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduDictScene eduDictScene)
    {
        List<EduDictScene> list = eduDictSceneService.selectEduDictSceneList(eduDictScene);
        ExcelUtil<EduDictScene> util = new ExcelUtil<EduDictScene>(EduDictScene.class);
        util.exportExcel(response, list, "故事场景字典数据");
    }

    /**
     * 获取故事场景字典详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:scene:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduDictSceneService.selectEduDictSceneById(id));
    }

    /**
     * 新增故事场景字典
     */
    @PreAuthorize("@ss.hasPermi('edu:scene:add')")
    @Log(title = "故事场景字典", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduDictScene eduDictScene)
    {
        return toAjax(eduDictSceneService.insertEduDictScene(eduDictScene));
    }

    /**
     * 修改故事场景字典
     */
    @PreAuthorize("@ss.hasPermi('edu:scene:edit')")
    @Log(title = "故事场景字典", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduDictScene eduDictScene)
    {
        return toAjax(eduDictSceneService.updateEduDictScene(eduDictScene));
    }

    /**
     * 删除故事场景字典
     */
    @PreAuthorize("@ss.hasPermi('edu:scene:remove')")
    @Log(title = "故事场景字典", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduDictSceneService.deleteEduDictSceneByIds(ids));
    }
}
