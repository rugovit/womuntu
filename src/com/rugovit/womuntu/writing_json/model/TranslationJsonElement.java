package com.rugovit.womuntu.writing_json.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranslationJsonElement {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("langcode")
    @Expose
    private String langcode;

    @SerializedName("client")
    @Expose
    private String client;

    @SerializedName("androidKey")
    @Expose
    private String androidKey;

    @SerializedName("iosKey")
    @Expose
    private String iosKey;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("created")
    @Expose
    private long created;

    @SerializedName("updated")
    @Expose
    private long updated;
    //////////////geters/setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLangcode() {
        return langcode;
    }

    public void setLangcode(String langcode) {
        this.langcode = langcode;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAndroidKey() {
        return androidKey;
    }

    public void setAndroidKey(String androidKey) {
        this.androidKey = androidKey;
    }

    public String getIosKey() {
        return iosKey;
    }

    public void setIosKey(String iosKey) {
        this.iosKey = iosKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }
}
