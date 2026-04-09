package com.iikun.anivideo.controller;

import com.iikun.anivideo.service.VideoIncomeService;
import com.iikun.common.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 视频收益流水表控制器
 * <p>
 * 处理UP主相关的收益流水相关操作
 * </p>
 */
@RestController
@RequestMapping("/video/income")
@RequiredArgsConstructor
@Tag(name = "视频收益", description = "处理UP主相关的收益流水相关操作")
public class VideoIncomeController {

    private final VideoIncomeService videoIncomeService;

    @Operation(summary = "管理端：新增收益记录", description = "手动补录收益流水（通常由订单/结算系统调用）")
    @PostMapping("/admin/add")
    public Result<?> add(@RequestBody @Valid IncomeAddRequest request) {
        videoIncomeService.addIncome(request.getVideoId(), request.getUserId(), request.getIncomeType(), request.getAmount());
        return Result.success();
    }

    @Operation(summary = "管理端：分页查询收益流水", description = "按 videoId/userId/incomeType 筛选")
    @GetMapping("/admin/page")
    public Result<Map<String, Object>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam(required = false) String videoId,
                                           @RequestParam(required = false) String userId,
                                           @RequestParam(required = false) Integer incomeType) {
        return Result.success(videoIncomeService.page(pageNum, pageSize, videoId, userId, incomeType));
    }

    @Operation(summary = "管理端：统计收益总额", description = "按 videoId/userId/incomeType 统计 amount 之和")
    @GetMapping("/admin/sum")
    public Result<Map<String, Object>> sum(@RequestParam(required = false) String videoId,
                                           @RequestParam(required = false) String userId,
                                           @RequestParam(required = false) Integer incomeType) {
        BigDecimal total = videoIncomeService.sumAmount(videoId, userId, incomeType);
        return Result.success(Map.of("totalAmount", total));
    }

    @Data
    public static class IncomeAddRequest {
        @NotBlank
        private String videoId;
        @NotBlank
        private String userId;
        @NotNull
        private Integer incomeType;
        @NotNull
        private BigDecimal amount;
    }

}
