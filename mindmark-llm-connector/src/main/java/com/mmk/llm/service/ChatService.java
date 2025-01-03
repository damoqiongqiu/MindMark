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

    Flux<String> chatStream(String msg);

    String embed(String msg, Set<String> fileIds) throws IOException, InterruptedException;

}
