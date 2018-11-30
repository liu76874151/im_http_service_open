package com.qunar.qchat.model.request;

public class SendWeRequest {
    private String from;
    private String fhost;
    private String to;
    private String thost;
    private String realJid;
   private String rhost;
    private String chatType;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFhost() {
        return fhost;
    }

    public void setFhost(String fhost) {
        this.fhost = fhost;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getThost() {
        return thost;
    }

    public void setThost(String thost) {
        this.thost = thost;
    }

    public String getRealJid() {
        return realJid;
    }

    public void setRealJid(String realJid) {
        this.realJid = realJid;
    }

    public String getRhost() {
        return rhost;
    }

    public void setRhost(String rhost) {
        this.rhost = rhost;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
}
