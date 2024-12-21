package com.mmk.llm.service;

import org.springframework.ai.image.Image;

/**
 * @author kenan
 * AI图像处理:主要包括图像生成、图像识别等等
 */
public interface ImgModelService {

    /**
     * 文生图
     *
     * @param msg
     * @return Image
     */
    Image genImg(String msg);

}
