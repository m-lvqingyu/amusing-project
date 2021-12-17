package com.amusing.start.map.enums;

/**
 * @author lv.QingYu
 */
public enum GaoDeGeoCodeLevel {

    NATION(1, "国家"),
    PROVINCE(2, "省"),
    CITY(3, "市"),
    DISTRICT(4, "区县"),
    DEVELOPMENT(5, "开发区"),
    TOWNSHIP(6, "乡镇"),
    VILLAGE(7, "村庄"),
    HOT_BUSINESS_DISTRICT(8, "热点商圈"),
    POINTS_OF_INTEREST(9, "兴趣点"),
    HOUSE_NUMBER(10, "门牌号"),
    UNIT_NUMBER(11, "单元号"),
    THE_WAY(12, "道路"),
    ROAD_INTERSECTION(13, "道路交叉路口"),
    BUS_STATION(14, "公交站台、地铁站"),
    UNKNOWN(15, "未知");

    GaoDeGeoCodeLevel(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private int key;

    private String value;

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static GaoDeGeoCodeLevel gaoDeGeoCodeLevel(String value) {
        for (GaoDeGeoCodeLevel level : GaoDeGeoCodeLevel.values()) {
            if (level.getValue().equals(value)) {
                return level;
            }
        }
        return GaoDeGeoCodeLevel.UNKNOWN;
    }

}
