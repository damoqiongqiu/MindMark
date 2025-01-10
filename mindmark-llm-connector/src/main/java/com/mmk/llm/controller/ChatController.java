package com.mmk.llm.controller;

import com.mmk.llm.annotation.ValidMessage;
import com.mmk.llm.constant.ModelType;
import com.mmk.llm.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 用来处理用户交互
 * @author 大漠穷秋
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/mind-mark")
public class ChatController {
    
    private final ChatService chatService;

    /**
     * 文本
     */
    @ValidMessage
    @GetMapping("chat")
    public String chat(
            @RequestParam(value = "msg", defaultValue = "") String msg,
            @RequestParam(value = "modelType", defaultValue = ModelType.ZHIPUAI) String modelType
    ) {
        return chatService.chat(msg, modelType);
    }

    /**
     * 文本流
     */
    @ValidMessage
    @GetMapping(value = "chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> chatStream(
            @RequestParam(value = "msg", defaultValue = "") String msg,
            @RequestParam(value = "modelType", defaultValue = ModelType.ZHIPUAI) String modelType
    ) {
        return chatService.chatStream(msg, modelType)
                .map(this::createResponse);
    }

    /**
     * 根据嵌入的文本进行查询
     */
    @ValidMessage
    @PostMapping("embedding")
    public String embed(
            @RequestBody EmbedRequestParam request,
            @RequestParam(value = "modelType", defaultValue = ModelType.ZHIPUAI) String modelType
    ) throws IOException, InterruptedException {
        return this.chatService.embed(request.getMsg(), request.getFileIds(), modelType);
    }

    /**
     * 辅助方法用于构建 JSON 响应
     */
    private Map<String, Object> createResponse(String content) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", content);
        return response;
    }

    @Getter
    @Setter
    public static class EmbedRequestParam {
        private String msg;
        private Set<String> fileIds;
    }
}
