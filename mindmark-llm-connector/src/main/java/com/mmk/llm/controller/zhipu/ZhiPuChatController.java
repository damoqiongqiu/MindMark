package com.mmk.llm.controller.zhipu;

import com.mmk.llm.annotation.ValidMessage;
import com.mmk.llm.service.zhipu.ZhiPuChatService;
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
public class ZhiPuChatController {
    private ZhiPuChatService zhiPuChatService;

    /**
     * 文本
     */
    @ValidMessage
    @GetMapping("chat")
    public String chat(@RequestParam(value = "msg", defaultValue = "") String msg) {
        return zhiPuChatService.chat(msg);
    }

    /**
     * 文本流
     */
    @ValidMessage
    @GetMapping(value = "chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> chatStream(@RequestParam(value = "msg", defaultValue = "") String msg) {
        return zhiPuChatService
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


    /**
     * 根据嵌入的文本进行查询
     * @param request
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @ValidMessage
    @PostMapping("embedding")
    public String embed(@RequestBody EmbedRequestParam request) throws IOException, InterruptedException {
        return this.zhiPuChatService.embed(request.getMsg(), request.getFileIds());
    }

    @Getter
    @Setter
    public static class EmbedRequestParam {
        private String msg;
        private Set<String> fileIds;
    }
}
