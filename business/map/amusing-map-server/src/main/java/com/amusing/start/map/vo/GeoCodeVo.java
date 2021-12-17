package com.amusing.start.map.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/12/14 18:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeoCodeVo {

    private String location;

    private Integer level;

}
