package com.qunar.qchat.model.result;
/**
 * @auth dongzd.zhang
 * @Date 2018/11/1 14:46
 */
public class UpdateMucNickResult {

    private String id;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "UpdateMucNickResult{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
