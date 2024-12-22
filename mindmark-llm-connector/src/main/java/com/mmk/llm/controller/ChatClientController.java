package com.mmk.llm.controller;

import com.mmk.llm.service.ChatClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
    @GetMapping("chatClient")
    public String chatClient(@RequestParam(value = "msg", defaultValue = "") String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            return "对话消息不能为空。";
        }
        return chatClientService.chatClient(msg);
    }

    /**
     * 文本流
     */
    @GetMapping(value = "chatClientStream", produces = "text/html;charset=utf-8")
    public Flux<String> chatClientStream(@RequestParam(value = "msg", defaultValue = "") String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            return Flux.just("对话消息不能为空。");
        }
        return chatClientService.chatClientStream(msg);
    }

}
