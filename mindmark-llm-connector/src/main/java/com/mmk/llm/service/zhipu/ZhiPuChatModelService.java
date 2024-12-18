package com.mmk.llm.service.zhipu;

import com.mmk.llm.ZhiPuConfig;
import com.mmk.llm.service.ChatModelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author 大漠穷秋
 */
@Slf4j
@Service
@AllArgsConstructor
public class ZhiPuChatModelService implements ChatModelService {

    /**
     * 智谱提供的文本嵌入模型
     * 输入和输出长度都有长度限制，参考官方文档：https://bigmodel.cn/dev/api/vector/embedding
     */
    private ZhiPuAiChatModel chatModel;

    private final ZhiPuConfig zpConfig;

    @Override
    public String chatModel(String msg) {
        return chatModel.call(
                    new Prompt(
                            msg,
                            ZhiPuAiChatOptions.builder().withModel(zpConfig.getModel()).withTemperature(zpConfig.getTemperature()).build()
                    )
                )
                .getResult()
                .getOutput()
                .getContent();
    }

    @Override
    public Flux<String> chatModelStream(String msg) {
        return chatModel.stream(
                    new Prompt(
                            msg,
                            ZhiPuAiChatOptions.builder().withModel(zpConfig.getModel()).withTemperature(zpConfig.getTemperature()).build()
                    )
                )
                .map(chatResponse -> chatResponse.getResults().get(0).getOutput().getContent());
    }
}
