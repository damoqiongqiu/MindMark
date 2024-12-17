package com.mmk.llm.service.zhipu;

import com.mmk.llm.service.ChatClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author 大漠穷秋
 */
@Slf4j
@Service
@AllArgsConstructor
public class ZhiPuChatClientService implements ChatClientService {

    private ChatClient chatClient;

    public String chatClient(String msg) {
        return chatClient.prompt().user(msg).call().content();
    }

    public Flux<String> chatClientStream(String msg) {
        return chatClient.prompt().user(msg).stream().content();
    }
}
