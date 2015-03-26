package com.znz.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by huangtao on 2015/1/27.
 */
public class ListChildVO {

    List<FileNodeVO> parentNodes ;

    public void setFileNodes(List<FileNodeVO> fileNodes) {
        this.fileNodes = fileNodes;
    }

    public void setParentNodes(List<FileNodeVO> parentNodes) {
        this.parentNodes = parentNodes;
    }

    List<FileNodeVO> fileNodes;


    public List<FileNodeVO> getParentNodes() {
        return parentNodes;
    }

    public List<FileNodeVO> getFileNodes() {
        return fileNodes;
    }
}
