package com.qunar.qchat.model.request;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @auth dongzd.zhang
 * @Date 2018/10/19 15:52
 */
public class UserStatusRequest implements IRequest {

    private String domain;
    private List<String> users;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getDomain() {

        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean isRequestValid() {

        return true;
    }

    @Override
    public String toString() {
        return "UserStatusRequest{" +
                "domain='" + domain + '\'' +
                ", users=" + users +
                '}';
    }
}
