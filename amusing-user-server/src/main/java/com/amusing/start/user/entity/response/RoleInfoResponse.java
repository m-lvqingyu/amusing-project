package com.amusing.start.user.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoleInfoResponse {

    private Integer id;

    private String name;

    private String code;

    private String description;

    private Integer status;

    private Integer isAdmin;

    private Long createTime;

}
