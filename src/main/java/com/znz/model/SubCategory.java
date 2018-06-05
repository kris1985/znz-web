package com.znz.model;

import java.io.Serializable;

public class SubCategory implements Serializable {
    private Integer id;

    private String name;

    private Integer sortId;

    private Integer parentId;

    private Integer categoryLevel;

    private String allFlag;

    private Integer partionCode;

    private Integer ppid;

    private String sortField;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(Integer categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public String getAllFlag() {
        return allFlag;
    }

    public void setAllFlag(String allFlag) {
        this.allFlag = allFlag;
    }

    public Integer getPartionCode() {
        return partionCode;
    }

    public void setPartionCode(Integer partionCode) {
        this.partionCode = partionCode;
    }

    public Integer getPpid() {
        return ppid;
    }

    public void setPpid(Integer ppid) {
        this.ppid = ppid;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
}