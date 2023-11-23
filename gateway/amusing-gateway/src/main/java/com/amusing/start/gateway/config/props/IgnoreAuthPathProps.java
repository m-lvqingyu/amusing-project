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
 * @description: 无需安全校验的路径集合
 * @since 2021/09/05
 */
@Configuration
@ConfigurationProperties(prefix = "ignore.auth")
@EnableConfigurationProperties({IgnoreAuthPathProps.class})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IgnoreAuthPathProps {

    private List<String> paths;

}
