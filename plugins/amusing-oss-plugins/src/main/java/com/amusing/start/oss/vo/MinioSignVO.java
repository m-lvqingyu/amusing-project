package com.amusing.start.oss.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description: 文件上传-签名
 * @since 2024/8/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MinioSignVO {

    private String bucket;

    private String xAmzDate;

    private String xAmzSignature;

    private String key;

    private String xAmzAlgorithm;

    private String xAmzCredential;

    private String policy;

}
