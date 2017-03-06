package com.znz.vo;

import lombok.Data;

/**
 * Created by Administrator on 2017/3/6.
 */
@Data
public class WatermarkVO {

    /**
     * 透明度 默认值：100， 表示 100%（不透明） 取值范围: [0-100]
     */
    private Integer t;

    /**
     * 位置，水印打在图的位置，详情参考下方区域数值对应图。
     取值范围：[nw,north,ne,west,center,east,ne,south]
     */
    private String g;

    /**
     * 水平边距, 就是距离图片边缘的水平距离， 这个参数只有当水印位置是左上，左中，左下， 右上，右中，右下才有意义
     默认值：10
     取值范围：[0 – 4096]
     单位：像素（px）
     */
    private Integer x;

    /**
     * 垂直边距, 就是距离图片边缘的垂直距离， 这个参数只有当水印位置是左上，中上， 右上，左下，中下，右下才有意义
     默认值：10
     取值范围：[0 – 4096]
     单位：像素(px)
     */
    private Integer y;

    /**
     * 中线垂直偏移，当水印位置在左中，中部，右中时，可以指定水印位置根据中线往上或者往下偏移。
     默认值：0
     取值范围：[-1000, 1000]
     单位：像素(px)
     */
    private Integer voffset;

    private String image;

    /**
     * 设置了P_10, 当主图是100x100, 水印图片大小就为10x10, 当主图变成了200x200，水印图片大小就为20x20
     */
    private Integer p;
}
