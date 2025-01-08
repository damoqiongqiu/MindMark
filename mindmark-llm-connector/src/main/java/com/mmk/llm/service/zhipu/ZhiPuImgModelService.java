package com.mmk.llm.service.zhipu;

import com.mmk.llm.service.ImgModelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.zhipuai.ZhiPuAiImageOptions;
import org.springframework.stereotype.Service;

/**
 * @author kenan
 * 智谱文生图服务
 * 详细请参考 https://bigmodel.cn/dev/api/image-model/cogview
 */
@Slf4j
@Service
@AllArgsConstructor
public class ZhiPuImgModelService implements ImgModelService {

    private final ImageModel imageModel;

    @Override
    public Image genImg(String msg) {
        // 如果需要自定义参数，可以参考 ZhiPuAiImageOptions，同时务必参考智谱官方文档（因 AI 开放平台迭代迅速，各家厂商配置项变化快）
        ImageResponse response = imageModel.call(
                new ImagePrompt(msg,
                        ZhiPuAiImageOptions.builder().model("cogview-3-plus")
                                .build())
        );
        return response.getResult().getOutput();
    }
}
