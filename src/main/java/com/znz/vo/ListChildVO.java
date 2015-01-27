package com.znz.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by huangtao on 2015/1/27.
 */
@Data
public class ListChildVO {

    List<FileNodeVO> parentNodes ;

    List<FileNodeVO> fileNodes;


}
