package com.znz.util;

import com.znz.vo.FileNodeVO;
import com.znz.vo.FileTreeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by huangtao on 2015/1/23.
 */
@Slf4j
public class MyFileUtil {

    public static void moveFiles(File srcfile,File destFile) throws IOException {
        File[] files = srcfile.listFiles();
        if(files==null)return ;
        for (File f : files) {
            try {
                if (f.isDirectory()) {// 判断是否文件夹
                    FileUtils.moveDirectoryToDirectory(f, destFile, false);
                } else {
                    FileUtils.moveFileToDirectory(f, destFile, false);
                }
            }catch (Exception e){
                log.error("文件已经存在："+f.getAbsolutePath());
                }

            }
    }

    /**
     * 显示所有子文件夹，用于tree 目录显示
     * @param rootFile
     * @param list
     */
    public static void  listFile(File rootFile, List<FileTreeVO> list) {
        File files [] = rootFile.listFiles();
        if(files==null || files.length<1){
            return;
        }
        FileTreeVO fileTreeVO;
        for(File f :files){
            if(f.isDirectory()){
                fileTreeVO = new FileTreeVO();
                fileTreeVO.setId(FilePathConverter.encode(f.getAbsolutePath()));
                fileTreeVO.setText(f.getName());
                fileTreeVO.setParent(FilePathConverter.encode(rootFile.getAbsolutePath()));
                list.add(fileTreeVO);
                listFile(f,list);
            }
        }
    }

    /**
     * 获取父目录
     * @param file
     * @param rootFile
     * @param nodeVOs
     */
    public static void getParentNode(File file,File rootFile,List<FileNodeVO> nodeVOs){
        if(file.getAbsolutePath().equals(rootFile.getAbsolutePath())){
            return;
        }
        File parentFile = file.getParentFile();
        if(parentFile.exists()){
            FileNodeVO node = new FileNodeVO();
            node.setPath(FilePathConverter.encode(parentFile.getAbsolutePath()));
            node.setName(parentFile.getName());
            nodeVOs.add(node);
        }
        getParentNode(parentFile,rootFile,nodeVOs);
    }
}
