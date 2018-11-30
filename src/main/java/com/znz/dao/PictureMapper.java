package com.znz.dao;

import com.znz.model.Picture;
import com.znz.vo.FileQueryVO;
import com.znz.vo.PageParameter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PictureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Picture record);


    Picture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Picture record);

    int updateByPrimaryKey(Picture record);

    List<Picture> selectByPage(FileQueryVO fileQueryVO);

    void deleteByPrimaryKeys(List<Long> pictureIds);

    List<Picture> selectByIds(@Param("listIds") List<Long> listIds, @Param("sortFiled") String sortFiled);

    List<Picture> selectByParam(FileQueryVO fileQueryVO);

    Picture selectByGid(String gid);

    int deleteByGid(String gid);

    List<Picture> selectByGids(List<String> gids);

    void deleteByGids(List<String> gids);

    List<Picture> selectBySimplePage(FileQueryVO fileQueryVO);

    List<Picture> selectByName(@Param("name") String name);

    List<Picture> selectByBookId(@Param("bookId") Long bookId);
}