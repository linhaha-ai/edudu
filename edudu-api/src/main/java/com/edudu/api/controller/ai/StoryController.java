package com.edudu.api.controller.ai;

import com.edudu.ai.service.StoryAiService;
import com.edudu.common.core.domain.AjaxResult;
import com.edudu.common.core.domain.model.EduLoginUser;
import com.edudu.common.utils.SecurityUtils;
import com.edudu.edu.domain.EduFavorite;
import com.edudu.edu.domain.EduStoryRecord;
import com.edudu.edu.service.IEduFavoriteService;
import com.edudu.edu.service.IEduStoryRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI故事接口
 */
@Slf4j
@RestController
@RequestMapping("/api/story")
@RequiredArgsConstructor
@Tag(name = "AI故事接口")
public class StoryController {

    private final StoryAiService storyAiService;

    @Autowired(required = false)
    private IEduStoryRecordService storyRecordService;

    @Autowired(required = false)
    private IEduFavoriteService favoriteService;

    /**
     * 生成故事
     */
    @PostMapping("/generate")
    @Operation(summary = "生成故事", description = "根据年龄、性别、场景、目标生成儿童故事")
    public AjaxResult generateStory(
            @Parameter(description = "孩子ID") @RequestParam Long childId,
            @Parameter(description = "孩子年龄") @RequestParam int age,
            @Parameter(description = "场景（bedtime/mealtime/tantrum/morning/education）") @RequestParam String scene,
            @Parameter(description = "教育目标") @RequestParam(required = false) String goal) {

        String result = storyAiService.generateStory(age, "未知", scene, goal);

        // 保存故事记录
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser != null && storyRecordService != null) {
            EduStoryRecord record = new EduStoryRecord();
            record.setUserId(loginUser.getUserId());
            record.setChildId(childId);
            record.setAge((long) age);
            record.setScene(scene);
            record.setGoal(goal);
            // 从结果中提取标题（简单处理：取前20个字符）
            String title = result.length() > 20 ? result.substring(0, 20) + "..." : result;
            record.setTitle(title);
            record.setContent(result);
            record.setMoral("从故事中学习成长");
            record.setModelType("openai");
            storyRecordService.insertEduStoryRecord(record);

            Map<String, Object> data = new HashMap<>();
            data.put("storyId", record.getId());
            data.put("content", result);
            return AjaxResult.success(data);
        }

        return AjaxResult.success(result);
    }

    /**
     * 生成睡前故事
     */
    @PostMapping("/bedtime")
    @Operation(summary = "生成睡前故事")
    public AjaxResult generateBedtimeStory(
            @Parameter(description = "孩子年龄") @RequestParam int age,
            @Parameter(description = "性别（男/女）") @RequestParam String gender,
            @Parameter(description = "教育目标") @RequestParam(required = false) String goal) {

        String result = storyAiService.generateBedtimeStory(age, gender, goal);
        return AjaxResult.success(result);
    }

    /**
     * 生成情绪引导故事
     */
    @PostMapping("/emotion")
    @Operation(summary = "生成情绪引导故事")
    public AjaxResult generateEmotionStory(
            @Parameter(description = "孩子年龄") @RequestParam int age,
            @Parameter(description = "性别（男/女）") @RequestParam String gender,
            @Parameter(description = "情绪类型") @RequestParam String emotion) {

        String result = storyAiService.generateEmotionStory(age, gender, emotion);
        return AjaxResult.success(result);
    }

    /**
     * 获取我的故事列表
     */
    @GetMapping("/my")
    @Operation(summary = "获取我的故事列表")
    public AjaxResult getMyStories(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "孩子ID") @RequestParam(required = false) Long childId) {

        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        EduStoryRecord query = new EduStoryRecord();
        query.setUserId(loginUser.getUserId());
        if (childId != null) {
            query.setChildId(childId);
        }

        PageHelper.startPage(page, pageSize);
        List<EduStoryRecord> list = storyRecordService.selectEduStoryRecordList(query);
        PageInfo<EduStoryRecord> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());

        return AjaxResult.success(result);
    }

    /**
     * 获取故事详情
     */
    @GetMapping("/{storyId}")
    @Operation(summary = "获取故事详情")
    public AjaxResult getStoryDetail(@PathVariable Long storyId) {
        EduStoryRecord record = storyRecordService.selectEduStoryRecordById(storyId);
        if (record == null) {
            return AjaxResult.error("故事不存在");
        }
        return AjaxResult.success(record);
    }

    /**
     * 收藏故事
     */
    @PostMapping("/{storyId}/favorite")
    @Operation(summary = "收藏故事")
    public AjaxResult favoriteStory(@PathVariable Long storyId) {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        EduStoryRecord story = storyRecordService.selectEduStoryRecordById(storyId);
        if (story == null) {
            return AjaxResult.error("故事不存在");
        }

        // 检查是否已收藏
        EduFavorite favorite = new EduFavorite();
        favorite.setUserId(loginUser.getUserId());
        favorite.setChildId(story.getChildId());
        favorite.setType("story");
        favorite.setSourceId(storyId);
        favorite.setTitle(story.getTitle());

        try {
            favoriteService.insertEduFavorite(favorite);
            return AjaxResult.success("收藏成功");
        } catch (Exception e) {
            return AjaxResult.error("已收藏，请勿重复操作");
        }
    }

    /**
     * 取消收藏
     */
    @PostMapping("/{storyId}/unfavorite")
    @Operation(summary = "取消收藏")
    public AjaxResult unfavoriteStory(@PathVariable Long storyId) {
        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        favoriteService.deleteEduFavoriteBySourceId(loginUser.getUserId(), "story", storyId);
        return AjaxResult.success("已取消收藏");
    }

    /**
     * 获取收藏列表
     */
    @GetMapping("/favorites")
    @Operation(summary = "获取收藏列表")
    public AjaxResult getFavoriteStories(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize) {

        EduLoginUser loginUser = SecurityUtils.getAppLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("请先登录");
        }

        EduFavorite query = new EduFavorite();
        query.setUserId(loginUser.getUserId());
        query.setType("story");

        PageHelper.startPage(page, pageSize);
        List<EduFavorite> list = favoriteService.selectEduFavoriteList(query);
        PageInfo<EduFavorite> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());

        return AjaxResult.success(result);
    }
}
