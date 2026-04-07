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
import com.edudu.edu.domain.EduFavorite;
import com.edudu.edu.service.IEduFavoriteService;
import com.edudu.common.utils.poi.ExcelUtil;
import com.edudu.common.core.page.TableDataInfo;

/**
 * 收藏Controller
 * 
 * @author edudu
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/edu/favorite")
public class EduFavoriteController extends BaseController
{
    @Autowired
    private IEduFavoriteService eduFavoriteService;

    /**
     * 查询收藏列表
     */
    @PreAuthorize("@ss.hasPermi('edu:favorite:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduFavorite eduFavorite)
    {
        startPage();
        List<EduFavorite> list = eduFavoriteService.selectEduFavoriteList(eduFavorite);
        return getDataTable(list);
    }

    /**
     * 导出收藏列表
     */
    @PreAuthorize("@ss.hasPermi('edu:favorite:export')")
    @Log(title = "收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduFavorite eduFavorite)
    {
        List<EduFavorite> list = eduFavoriteService.selectEduFavoriteList(eduFavorite);
        ExcelUtil<EduFavorite> util = new ExcelUtil<EduFavorite>(EduFavorite.class);
        util.exportExcel(response, list, "收藏数据");
    }

    /**
     * 获取收藏详细信息
     */
    @PreAuthorize("@ss.hasPermi('edu:favorite:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eduFavoriteService.selectEduFavoriteById(id));
    }

    /**
     * 新增收藏
     */
    @PreAuthorize("@ss.hasPermi('edu:favorite:add')")
    @Log(title = "收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduFavorite eduFavorite)
    {
        return toAjax(eduFavoriteService.insertEduFavorite(eduFavorite));
    }

    /**
     * 修改收藏
     */
    @PreAuthorize("@ss.hasPermi('edu:favorite:edit')")
    @Log(title = "收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduFavorite eduFavorite)
    {
        return toAjax(eduFavoriteService.updateEduFavorite(eduFavorite));
    }

    /**
     * 删除收藏
     */
    @PreAuthorize("@ss.hasPermi('edu:favorite:remove')")
    @Log(title = "收藏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eduFavoriteService.deleteEduFavoriteByIds(ids));
    }
}
