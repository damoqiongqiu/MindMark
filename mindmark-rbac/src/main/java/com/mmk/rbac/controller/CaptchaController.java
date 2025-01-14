package com.mmk.rbac.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.annotation.Resource;
import javax.imageio.ImageIO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码
 * TODO:这款验证码组件有点老，需要换一个更新一点的。
 * @author 大漠穷秋
 */
@Controller
@RequestMapping("/mind-mark/auth/captcha")
public class CaptchaController {
    private static final Logger logger = LoggerFactory.getLogger(CaptchaController.class);

    //文本型验证码
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    //数学计算型验证码
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    /**
     * 获取验证码，有两种类型：math 或者 char 。
     * 获取验证码的请求默认开启 Session ，这个会话用来存储验证码结果，以便与用户输入的内容进行比对，
     * 后续的 /login 和其它操作都会复用这个 HttpSession 。
     * 在与 Shiro 整合时，这里获得的 Session 实例是被 Shiro 包装过的 ShiroHttpSession 类型。
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "/captchaImage")
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            HttpSession session = request.getSession();
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("image/jpeg");

            String capStr = null;   //验证码
            String capResult = null;//结果，会被存在 session 中
            BufferedImage bufferedImg = null;
            String type = request.getParameter("type");
            if ("math".equals(type)) {
                String capText = captchaProducerMath.createText();
                capStr = capText.substring(0, capText.lastIndexOf("@"));
                capResult = capText.substring(capText.lastIndexOf("@") + 1);
                bufferedImg = captchaProducerMath.createImage(capStr);
            } else if ("char".equals(type)) {
                capStr = capResult = captchaProducer.createText();
                bufferedImg = captchaProducer.createImage(capStr);
            }
            
            session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capResult);
            out = response.getOutputStream();
            ImageIO.write(bufferedImg, "jpg", out);
            out.flush();
        } catch (Exception e) {
            logger.debug(e.getMessage(),e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                logger.debug(e.getMessage(),e);
            }
        }
        return null;
    }
}