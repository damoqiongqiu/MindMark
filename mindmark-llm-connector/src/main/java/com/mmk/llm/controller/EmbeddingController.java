package com.mmk.llm.controller;

import com.mmk.llm.service.EmbeddingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 根据嵌入的文本进行查询
     * @param msg
     * @return
     */
    @GetMapping("/embedding")
    public String embed(@RequestParam(value = "msg", defaultValue = "Spring") String msg) {
        return this.embeddingService.embed(msg);
    }
}