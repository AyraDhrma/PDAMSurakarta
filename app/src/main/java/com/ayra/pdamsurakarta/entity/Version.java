package com.ayra.pdamsurakarta.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Version {

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("LinkApp")
    @Expose
    private String linkApp;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("uri")
    @Expose
    private String uri;

    public Version(String version, String linkApp, String info, String uri) {
        super();
        this.version = version;
        this.linkApp = linkApp;
        this.info = info;
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLinkApp() {
        return linkApp;
    }

    public void setLinkApp(String linkApp) {
        this.linkApp = linkApp;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
