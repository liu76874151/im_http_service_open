package com.qunar.qchat.model.request;

import com.qunar.qchat.constants.Config;
import com.qunar.qchat.constants.ProfileConstants;

import java.util.Objects;

public class GetProfileRequest {
    public String user;
    public String domain;
    public int version;
    public String mood;
    public String url;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDomain() {
        if(domain == null) {
            return Config.getProperty("default_host");
        }
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getMood() {
        if(mood == null) {
            return "";
        }
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getUrl() {
        if (url == null) {
            return ProfileConstants.DEFAULT_URL;
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
