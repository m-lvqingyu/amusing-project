package com.amusing.start.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/5
 */
@Data
@ConfigurationProperties(value = "amusing.upload.file")
public class FileIdProperties {

    private Long workerId;

    private Long datacenterId;

}
