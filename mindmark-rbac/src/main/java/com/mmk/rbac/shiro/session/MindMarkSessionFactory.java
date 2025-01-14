package com.mmk.rbac.shiro.session;

import com.mmk.core.utils.IPUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
 * MindMark 自定义的 Session 工厂类，扩展 SimpleSessionFactory ，把一些额外的属性设置到 Session 实例的 attributes 里面。
 * Shiro 在运行时会使用此类来创建 Session 实例，配置项在 ShiroConfig.java 中。
 * @author 大漠穷秋
 */
public class MindMarkSessionFactory extends SimpleSessionFactory {
    @Override
    public Session createSession(SessionContext sessionContext) {
        Session session=super.createSession(sessionContext);
        if (sessionContext instanceof WebSessionContext) {
            WebSessionContext webSessionContext = (WebSessionContext) sessionContext;
            HttpServletRequest request = (HttpServletRequest) webSessionContext.getServletRequest();
            String ip= IPUtil.getIpAddr(request);
            session.setAttribute("host",ip);

            ServletRequest servletRequest=webSessionContext.getServletRequest();
            if(servletRequest instanceof HttpServletRequest){
                String userAgentString=((HttpServletRequest)servletRequest).getHeader("User-Agent");
                session.setAttribute("userAgent",userAgentString);

                UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
                String os = userAgent.getOperatingSystem().getName();
                String browser = userAgent.getBrowser().getName();
                session.setAttribute("os",os);
                session.setAttribute("browser",browser);
            }
        }
        return session;
    }
}
