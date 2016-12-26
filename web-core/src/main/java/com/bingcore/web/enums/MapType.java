package com.bingcore.web.enums;

import com.google.common.base.Objects;

/**
 * <pre>
 * 功能描述：地图数据类型
 *
 * </pre>
 * Author: xubing
 * DateTime: 2016-05-11 下午4:05
 */
public enum MapType {
    GOODS_ONE(1, "手机"),
    GOODS_TWO(2, "笔记本电脑"),
    GOODS_THREE(3, "平板电脑");

    private int type;
    private String name;

    private MapType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static MapType getByType(int type) {
        for (MapType at : MapType.values()) {
            if (at.type == type) {
                return at;
            }
        }
        return null;
    }

    public static MapType getByName(String name) {
        for (MapType at : MapType.values()) {
            if (Objects.equal(at.getName(), name)) {
                return at;
            }
        }
        return null;
    }
}
