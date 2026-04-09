package com.edudu.edu.service.impl;

import java.util.List;
import com.edudu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edudu.edu.mapper.EduFavoriteMapper;
import com.edudu.edu.domain.EduFavorite;
import com.edudu.edu.service.IEduFavoriteService;

/**
 * 收藏Service业务层处理
 * 
 * @author edudu
 * @date 2026-04-07
 */
@Service
public class EduFavoriteServiceImpl implements IEduFavoriteService 
{
    @Autowired
    private EduFavoriteMapper eduFavoriteMapper;

    /**
     * 查询收藏
     * 
     * @param id 收藏主键
     * @return 收藏
     */
    @Override
    public EduFavorite selectEduFavoriteById(Long id)
    {
        return eduFavoriteMapper.selectEduFavoriteById(id);
    }

    /**
     * 查询收藏列表
     * 
     * @param eduFavorite 收藏
     * @return 收藏
     */
    @Override
    public List<EduFavorite> selectEduFavoriteList(EduFavorite eduFavorite)
    {
        return eduFavoriteMapper.selectEduFavoriteList(eduFavorite);
    }

    /**
     * 新增收藏
     * 
     * @param eduFavorite 收藏
     * @return 结果
     */
    @Override
    public int insertEduFavorite(EduFavorite eduFavorite)
    {
        eduFavorite.setCreateTime(DateUtils.getNowDate());
        return eduFavoriteMapper.insertEduFavorite(eduFavorite);
    }

    /**
     * 修改收藏
     * 
     * @param eduFavorite 收藏
     * @return 结果
     */
    @Override
    public int updateEduFavorite(EduFavorite eduFavorite)
    {
        return eduFavoriteMapper.updateEduFavorite(eduFavorite);
    }

    /**
     * 批量删除收藏
     * 
     * @param ids 需要删除的收藏主键
     * @return 结果
     */
    @Override
    public int deleteEduFavoriteByIds(Long[] ids)
    {
        return eduFavoriteMapper.deleteEduFavoriteByIds(ids);
    }

    /**
     * 删除收藏信息
     * 
     * @param id 收藏主键
     * @return 结果
     */
    @Override
    public int deleteEduFavoriteById(Long id) {
        return eduFavoriteMapper.deleteEduFavoriteById(id);
    }

    /**
     * 根据用户ID、类型、来源ID删除收藏
     */
    @Override
    public int deleteEduFavoriteBySourceId(Long userId, String type, Long sourceId) {
        return eduFavoriteMapper.deleteEduFavoriteBySourceId(userId, type, sourceId);
    }
}
