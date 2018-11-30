package com.qunar.qchat.model.request;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/6 12:15
 */
public class GetInviteInfoRequest {

    private String user;
    private String domain;
    private Integer time = -1;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "GetInviteInfoRequest{" +
                "user='" + user + '\'' +
                ", domain='" + domain + '\'' +
                ", time=" + time +
                '}';
    }
}
