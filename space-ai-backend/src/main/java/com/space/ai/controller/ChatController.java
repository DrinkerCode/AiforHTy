package com.space.ai.controller;

import com.space.ai.model.ChatRequest;
import com.space.ai.model.ChatResponse;
import com.space.ai.service.AliyunAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * AI 问答控制器
 */
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {
    
    private final AliyunAiService aiService;
    
    /**
     * 航天知识问答接口
     */
    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        return aiService.chat(request);
    }
    
    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public String health() {
        return "AI Service is running!";
    }
}
