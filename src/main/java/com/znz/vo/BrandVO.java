package com.znz.vo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.znz.model.SubCategory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

@Getter
@Setter
public class BrandVO {

    private Integer id;
    private String name;
    private Integer parentId;
    private String shortName;
    private String letter;
    private Integer cityCode;
    private String pinyin;

    final static   Cache<String, Object> brandCache = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build();



    public static Optional<Object>    geta() throws ExecutionException {
        Optional<Object> obj = (Optional<Object>)brandCache.get("aa", () -> {
           return Optional.ofNullable("a");
        });

        return obj;
    }

    public static void main(String[] args) {
        char letter ="未知".toUpperCase().charAt(0);
        if(letter<65 || letter>90 ){
            System.out.println("00000");
        }
    }
}
