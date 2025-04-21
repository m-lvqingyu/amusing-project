package com.amusing.start.oss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description: 文件上传结果
 * @since 2024/8/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors
public class UploadResultDto {

    /**
     * 文件预览URL
     */
    private String preUrl;

}
