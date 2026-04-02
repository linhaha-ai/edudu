package com.edudu.api.controller.ai;

import com.edudu.ai.service.StoryAiService;
import com.edudu.common.core.domain.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * AI故事接口
 */
@Slf4j
@RestController
@RequestMapping("/api/story")
@RequiredArgsConstructor
@Tag(name = "AI故事接口")
@Validated
public class StoryController {

    private final StoryAiService storyAiService;

    /**
     * 生成故事
     */
    @PostMapping("/generate")
    @Operation(summary = "生成故事", description = "根据年龄、性别、场景、目标生成儿童故事")
    public AjaxResult generateStory(
            @Parameter(description = "孩子年龄") @RequestParam int age,
            @Parameter(description = "性别（男/女）") @RequestParam String gender,
            @Parameter(description = "场景（睡前/吃饭/发脾气/起床/早教）") @RequestParam String scene,
            @Parameter(description = "教育目标（学会分享/勇敢/不哭闹/诚实/友爱）") @RequestParam(required = false) String goal) {
        
        String result = storyAiService.generateStory(age, gender, scene, goal);
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
     * 获取当前AI模型信息
     */
    @GetMapping("/model")
    @Operation(summary = "获取AI模型信息")
    public AjaxResult getModelInfo() {
        return AjaxResult.success("model", storyAiService.getModelName());
    }
}
