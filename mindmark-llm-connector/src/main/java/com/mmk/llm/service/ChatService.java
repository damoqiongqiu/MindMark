package com.mmk.llm.service;

import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Set;

/**
 * @author 大漠穷秋
 * 处理用户交互
 */
public interface ChatService {

    String chat(String msg);
    String chat(String msg, String modelType);

    Flux<String> chatStream(String msg);
    Flux<String> chatStream(String msg, String modelType);

    String embed(String msg, Set<String> fileIds) throws IOException, InterruptedException;
    String embed(String msg, Set<String> fileIds, String modelType) throws IOException, InterruptedException;

}
