package com.edudu.api.controller.ai;

import com.edudu.ai.service.ParentAiService;
import com.edudu.common.core.domain.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * AI家长助手接口
 */
@Slf4j
@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
@Tag(name = "AI家长助手接口")
public class ParentController {

    private final ParentAiService parentAiService;

    /**
     * 提问
     */
    @PostMapping("/ask")
    @Operation(summary = "家长提问", description = "提交育儿问题，获取AI回答")
    public AjaxResult askQuestion(
            @Parameter(description = "孩子年龄") @RequestParam int age,
            @Parameter(description = "问题描述") @RequestParam String question) {
        
        String result = parentAiService.answerQuestion(age, question);
        return AjaxResult.success(result);
    }

    /**
     * 生成成长报告
     */
    @PostMapping("/report")
    @Operation(summary = "生成成长报告", description = "根据行为数据生成AI成长分析报告")
    public AjaxResult generateGrowthReport(
            @Parameter(description = "孩子年龄") @RequestParam int age,
            @Parameter(description = "行为数据JSON") @RequestParam String data) {
        
        String result = parentAiService.generateGrowthReport(age, data);
        return AjaxResult.success(result);
    }
}
