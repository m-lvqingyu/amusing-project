package com.amusing.start.gateway.config.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 角色-菜单映射
 * @since 2023/08/26
 */
@Configuration
@ConfigurationProperties(prefix = "role.path")
@EnableConfigurationProperties({RolePathMappingProps.class})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePathMappingProps {

    /**
     * 角色-菜单映射
     */
    private List<RolePathMapping> mapping;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RolePathMapping {
        
        /**
         * 角色Code
         */
        private Integer code;

        /**
         * 请求Path
         */
        private List<String> uris;

    }

}
