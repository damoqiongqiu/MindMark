package com.mmk.llm.controller;

import com.mmk.llm.service.EmbeddingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 大漠穷秋
 * 嵌入模型控制器
 * 用来处理文本嵌入相关的操作
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("llm")
public class EmbeddingController {

    private EmbeddingService embeddingService;

    @GetMapping("/embedding")
    public Map embed(@RequestParam(value = "msg", defaultValue = "Spring") String msg) {
        EmbeddingResponse embeddingResponse = this.embeddingService.embed(msg);
        return Map.of("embedding", embeddingResponse);
    }
}