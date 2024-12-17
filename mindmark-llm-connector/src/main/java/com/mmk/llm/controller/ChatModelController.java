package com.mmk.llm.controller;

import com.mmk.llm.service.ChatModelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author 大漠穷秋
 * 对话模型控制器
 * 用来控制大模型本身的行为，如：切换不同的大模型、配置默认参数。
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("llm")
public class ChatModelController {

    private ChatModelService chatModelService;

    @GetMapping("chatModel")
    public String chatModel(@RequestParam(value = "msg", defaultValue = "") String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            return "对话消息不能为空。";
        }
        return chatModelService.chatModel(msg);
    }

    @GetMapping(value = "chatModelStream", produces = "text/html;charset=utf-8")
    public Flux<String> chatModelStream(@RequestParam(value = "msg", defaultValue = "") String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            return Flux.just("对话消息不能为空。");
        }
        return chatModelService.chatModelStream(msg);
    }

}
