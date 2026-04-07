package com.edudu.edu.mapper;

import java.util.List;
import com.edudu.edu.domain.EduFavorite;

/**
 * 收藏Mapper接口
 * 
 * @author edudu
 * @date 2026-04-07
 */
public interface EduFavoriteMapper 
{
    /**
     * 查询收藏
     * 
     * @param id 收藏主键
     * @return 收藏
     */
    public EduFavorite selectEduFavoriteById(Long id);

    /**
     * 查询收藏列表
     * 
     * @param eduFavorite 收藏
     * @return 收藏集合
     */
    public List<EduFavorite> selectEduFavoriteList(EduFavorite eduFavorite);

    /**
     * 新增收藏
     * 
     * @param eduFavorite 收藏
     * @return 结果
     */
    public int insertEduFavorite(EduFavorite eduFavorite);

    /**
     * 修改收藏
     * 
     * @param eduFavorite 收藏
     * @return 结果
     */
    public int updateEduFavorite(EduFavorite eduFavorite);

    /**
     * 删除收藏
     * 
     * @param id 收藏主键
     * @return 结果
     */
    public int deleteEduFavoriteById(Long id);

    /**
     * 批量删除收藏
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEduFavoriteByIds(Long[] ids);
}
