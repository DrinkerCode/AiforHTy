package com.space.ai.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.space.ai.model.ChatRequest;
import com.space.ai.model.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 阿里云大模型服务
 */
@Slf4j
@Service
public class AliyunAiService {
    
    @Value("${aliyun.api-key}")
    private String apiKey;
    
    @Value("${aliyun.api-endpoint}")
    private String apiEndpoint;
    
    @Value("${aliyun.model:qwen-max}")
    private String model;
    
    private final HttpClient httpClient;
    
    // 航天知识系统提示词
    private static final String SPACE_SYSTEM_PROMPT = """
        你是一位专业的航天知识专家，专注于回答与航天相关的各种问题。你的知识领域包括但不限于：
        
        1. 火箭发射技术（长征系列、SpaceX、ULA 等）
        2. 卫星技术与应用（通信、导航、遥感等）
        3. 载人航天（空间站、宇航员、太空任务等）
        4. 深空探测（月球探测、火星探测、小行星探测等）
        5. 航天历史与重要事件
        6. 航天器设计与原理
        7. 轨道力学与航天动力学
        8. 国际航天合作与竞争
        9. 商业航天发展
        10. 未来航天计划与展望
        
        请用专业但易懂的语言回答问题，适当使用数据和技术术语，但要确保解释清晰。
        如果问题超出你的知识范围或不够明确，请礼貌地说明并尝试提供相关建议。
        回答时请注意准确性，对于不确定的信息要说明可能存在的不确定性。
        """;
    
    public AliyunAiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }
    
    /**
     * 调用阿里云大模型进行问答
     */
    public ChatResponse chat(ChatRequest request) {
        String sessionId = request.getSessionId() != null ? 
                          request.getSessionId() : UUID.randomUUID().toString();
        
        String systemPrompt = request.getSystemPrompt() != null ? 
                             request.getSystemPrompt() : SPACE_SYSTEM_PROMPT;
        
        try {
            // 构建请求体
            JSONObject requestBody = buildRequestBody(request.getQuestion(), systemPrompt);
            
            // 创建 HTTP 请求
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(apiEndpoint))
                    .timeout(Duration.ofSeconds(60))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toJSONString()))
                    .build();
            
            // 发送请求
            HttpResponse<String> response = httpClient.send(httpRequest, 
                    HttpResponse.BodyHandlers.ofString());
            
            // 解析响应
            return parseResponse(response.body(), sessionId);
            
        } catch (Exception e) {
            log.error("调用阿里云大模型失败", e);
            return ChatResponse.builder()
                    .success(false)
                    .errorMessage("AI 服务调用失败：" + e.getMessage())
                    .sessionId(sessionId)
                    .model(model)
                    .build();
        }
    }
    
    /**
     * 构建请求体
     */
    private JSONObject buildRequestBody(String question, String systemPrompt) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        
        // 构建 messages 数组
        JSONArray messages = new JSONArray();
        
        // 系统消息
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemPrompt);
        messages.add(systemMessage);
        
        // 用户消息
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", question);
        messages.add(userMessage);
        
        requestBody.put("input", new JSONObject().fluentPut("messages", messages));
        
        // 参数配置
        JSONObject parameters = new JSONObject();
        parameters.put("result_format", "message");
        requestBody.put("parameters", parameters);
        
        return requestBody;
    }
    
    /**
     * 解析响应
     */
    private ChatResponse parseResponse(String responseBody, String sessionId) {
        try {
            JSONObject response = JSON.parseObject(responseBody);
            
            // 检查是否有错误
            if (response.containsKey("code")) {
                String code = response.getString("code");
                String message = response.getString("message");
                return ChatResponse.builder()
                        .success(false)
                        .errorMessage("API 错误 [" + code + "]: " + message)
                        .sessionId(sessionId)
                        .model(model)
                        .build();
            }
            
            // 提取回答内容
            if (response.containsKey("output") && 
                response.getJSONObject("output").containsKey("choices")) {
                
                JSONArray choices = response.getJSONObject("output")
                        .getJSONArray("choices");
                
                if (choices != null && !choices.isEmpty()) {
                    JSONObject firstChoice = choices.getJSONObject(0);
                    JSONObject message = firstChoice.getJSONObject("message");
                    
                    if (message != null && message.containsKey("content")) {
                        String answer = message.getString("content");
                        return ChatResponse.builder()
                                .success(true)
                                .answer(answer)
                                .sessionId(sessionId)
                                .model(model)
                                .build();
                    }
                }
            }
            
            return ChatResponse.builder()
                    .success(false)
                    .errorMessage("无法解析 AI 响应")
                    .sessionId(sessionId)
                    .model(model)
                    .build();
                    
        } catch (Exception e) {
            log.error("解析响应失败", e);
            return ChatResponse.builder()
                    .success(false)
                    .errorMessage("响应解析失败：" + e.getMessage())
                    .sessionId(sessionId)
                    .model(model)
                    .build();
        }
    }
}
