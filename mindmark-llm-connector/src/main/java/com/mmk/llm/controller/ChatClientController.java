package com.mmk.llm.controller;

import com.mmk.llm.service.ChatClientService;
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
 * @author 大漠穷秋
 * 用来处理用户交互
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/mind-mark")
public class ChatClientController {

    private ChatClientService chatClientService;

    /**
     * 文本
     */
    @GetMapping("chat")
    public String chatClient(@RequestParam(value = "msg", defaultValue = "") String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            return "对话消息不能为空。";
        }
        return chatClientService.chat(msg);
    }

    /**
     * 文本流
     */
    @GetMapping(value = "chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> chatClientStream(@RequestParam(value = "msg", defaultValue = "") String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            return Flux.just(createResponse( "对话消息不能为空。"));
        }
        return chatClientService
                .chatStream(msg)
                .map(data -> createResponse( data));
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


    /**
     * 根据嵌入的文本进行查询
     * @param request
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping("embedding")
    public String embed(@RequestBody EmbedRequestParam request) throws IOException, InterruptedException {
        String msg = request.getMsg();
        if (msg == null || msg.trim().isEmpty()) {
            return "对话消息不能为空。";
        }

        Set<String> fileIds = request.getFileIds();
        return this.chatClientService.embed(msg, fileIds);
    }

    @Getter
    @Setter
    public static class EmbedRequestParam {
        private String msg;
        private Set<String> fileIds;
    }
}
