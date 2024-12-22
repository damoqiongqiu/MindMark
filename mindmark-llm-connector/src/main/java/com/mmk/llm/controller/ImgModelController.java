package com.mmk.llm.controller;

import com.mmk.llm.service.ImgModelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.Image;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kenan
 * 图片模型控制器
 * 用来处理智谱 AI 的图像生成模型
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/mind-mark")
public class ImgModelController {

    private ImgModelService imgModelService;

    /**
     * 根据文本生成图片
     *
     * @param msg
     * @return image: 图片的 base64 编码或者图片的 url
     */
    @GetMapping("/genImg")
    public Image genImg(@RequestParam(value = "msg", defaultValue = "根据大漠孤烟直，长河落日圆这句古诗生成一张图片") String msg) {
        return this.imgModelService.genImg(msg);
    }
}