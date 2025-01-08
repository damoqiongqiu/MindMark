package com.mmk.llm.service.zhipu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author 大漠穷秋
 */
@Slf4j
@Service
public class ZhiPuChatModelService implements com.mmk.llm.service.ChatModelService {
    @Autowired
    private ZhiPuAiChatModel zhiPuAiChatModel;

    @Value("${spring.ai.zhipuai.chat.options.model}")
    private String modelName;

    @Value("${spring.ai.zhipuai.chat.options.temperature}")
    private Double temperature;

    @Override
    public String chatModel(String msg) {
        return zhiPuAiChatModel.call(
                    new Prompt(
                            msg,
                            ZhiPuAiChatOptions.builder().model(modelName).temperature(temperature).build()
                    )
                )
                .getResult()
                .getOutput()
                .getContent();
    }

    @Override
    public Flux<String> chatModelStream(String msg) {
        return zhiPuAiChatModel.stream(
                    new Prompt(
                            msg,
                            ZhiPuAiChatOptions.builder().model(modelName).temperature(temperature).build()
                    )
                )
                .map(chatResponse -> chatResponse.getResults().get(0).getOutput().getContent());
    }
}
