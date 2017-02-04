package com.znz.vo;

import lombok.Data;
import lombok.ToString;

/**
 * Created by huangtao on 2015/1/27.
 */
@ToString
public class FileNodeVO {
    private String path; //文件绝对路径
    private String url; //图片显示路径
    private String thumbUrl; //图片显示路径
    private String name;
    private long lastModified;
    private boolean directory;
    private boolean selected;

    public void setPath(String path) {
        this.path = path;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getName() {
        return name;
    }

    public long getLastModified() {
        return lastModified;
    }

    public boolean isDirectory() {
        return directory;
    }

    public boolean isSelected() {
        return selected;
    }
}
