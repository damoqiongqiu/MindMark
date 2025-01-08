package com.mmk.llm.controller.openai;

import com.mmk.llm.annotation.ValidMessage;
import com.mmk.llm.service.openai.OpenAiChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 大漠穷秋
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/mind-mark/open-ai")
public class OpenAiChatController {
    private OpenAiChatService openAiChatService;

    /**
     * 文本
     */
    @ValidMessage
    @GetMapping("chat")
    public String chat(@RequestParam(value = "msg", defaultValue = "") String msg) {
        return openAiChatService.chat(msg);
    }

    /**
     * 文本流
     */
    @ValidMessage
    @GetMapping(value = "chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> chatStream(@RequestParam(value = "msg", defaultValue = "") String msg) {
        return openAiChatService
                .chatStream(msg)
                .map(data -> createResponse(data));
    }

    /**
     * 辅助方法用于构建 JSON 响应
     * @param content
     * @return
     */
    private Map<String, Object> createResponse(String content) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", content);
        return response;
    }
}