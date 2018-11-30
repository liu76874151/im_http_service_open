package com.qunar.qchat.model.request;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/1 12:09
 */
public class UpdateMucNickRequest {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UpdateMucNickRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
