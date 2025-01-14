package com.mmk.rbac.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.Date;

/**
 * 此实体类用于持久化 Session 到数据库。
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "mindmark_rbac_session")
public class MindMarkSessionEntity implements Serializable {
    @Id
    @Column(name = "session_id")
    private String sessionId;

    @Column(name="app_name")
    private String appName;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="user_name")
    private String userName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="creation_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="expiry_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiryTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_access_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastAccessTime;

    @Column(name="max_inactive_interval")
    private Integer maxInactiveInteval;

    //Session 有效时间，单位 ms
    @Column(name="timeout")
    private Long timeout;

    @Column(name="expired")
    private Boolean expired=false;

    @Column(name="host")
    private String host;

    @Column(name="os")
    private String os;

    @Column(name="browser")
    private String browser;

    @Column(name="userAgent")
    private String userAgent;

    /**
     * Session 中的所有 K-V 都序列化在这个 JSON 字符串中。
     * 为了跨项目场景下反序列化失败，此 JSON 字符串中的数据只包含 Java 基本类型，不处理自定义的类。
     */
    @Lob
    @Column(name="session_data",columnDefinition="text")
    private String sessionData;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSessionData() {
        return sessionData;
    }

    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }

    public Integer getMaxInactiveInteval() {
        return maxInactiveInteval;
    }

    public void setMaxInactiveInteval(Integer maxInactiveInteval) {
        this.maxInactiveInteval = maxInactiveInteval;
    }
}