package com.znz.vo;

import lombok.Data;

/**
 * Created by huangtao on 2015/1/27.
 */
public class FileTreeVO {
    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getParent() {
        return parent;
    }

    private String id;
    private String text;
    private String parent;
    private long lastModified;

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
