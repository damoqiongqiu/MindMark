package com.mmk.llm.controller;

import com.mmk.llm.service.ChatClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;

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
    public Flux<String> chatClientStream(@RequestParam(value = "msg", defaultValue = "") String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            return Flux.just("对话消息不能为空。");
        }
        return chatClientService.chatStream(msg);
    }

    /**
     * 根据嵌入的文本进行查询
     * @param msg
     * @return
     */
    @GetMapping("embedding")
    public String embed(@RequestParam(value = "msg", defaultValue = "") String msg) throws IOException, InterruptedException {
        if (msg == null || msg.trim().isEmpty()) {
            return "对话消息不能为空。";
        }
        return this.chatClientService.embed(msg);
    }
}
