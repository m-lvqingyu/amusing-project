package com.amusing.start.order.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Lv.QingYu
 * @since 2025/3/6
 */
@Data
@Component
@ConfigurationProperties(prefix = "order.snowflake")
public class SnowflakeProps {

    private Long worker;

    private Long center;

}
