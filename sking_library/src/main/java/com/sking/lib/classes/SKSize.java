package com.sking.lib.classes;

/**
 * 图片尺寸
 * Created by 谁说青春不能错 on 2016/11/29.
 */

public class SKSize {
    private int width;// 宽
    private int height;// 高
    /**
     * 实例化一个图片尺寸
     *
     * @param width
     *            宽
     * @param height
     *            高
     */
    public SKSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 宽
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * 高
     *
     * @return
     */
    public int getHeight() {
        return height;
    }
}
