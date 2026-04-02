package com.edudu.api.controller.ai;

import com.edudu.ai.service.TaskAiService;
import com.edudu.common.core.domain.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * AI早教任务接口
 */
@Slf4j
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@Tag(name = "AI早教任务接口")
public class TaskController {

    private final TaskAiService taskAiService;

    /**
     * 生成每日早教任务
     */
    @PostMapping("/generate")
    @Operation(summary = "生成早教任务", description = "根据孩子年龄和主题生成早教任务")
    public AjaxResult generateTask(
            @Parameter(description = "孩子年龄") @RequestParam int age,
            @Parameter(description = "主题（颜色/动物/情绪/语言/数字）") @RequestParam String theme) {
        
        String result = taskAiService.generateTask(age, theme);
        return AjaxResult.success(result);
    }

    /**
     * 根据年龄段生成任务
     */
    @PostMapping("/daily")
    @Operation(summary = "生成每日任务", description = "根据孩子年龄段自动匹配合适的主题")
    public AjaxResult generateDailyTask(
            @Parameter(description = "孩子年龄") @RequestParam int age) {
        
        String result = taskAiService.generateTaskByAgeGroup(age);
        return AjaxResult.success(result);
    }
}
