package com.space.ai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 问答请求模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    
    /**
     * 用户问题
     */
    private String question;
    
    /**
     * 会话 ID（可选，用于多轮对话）
     */
    private String sessionId;
    
    /**
     * 系统提示词（可选）
     */
    private String systemPrompt;
}
