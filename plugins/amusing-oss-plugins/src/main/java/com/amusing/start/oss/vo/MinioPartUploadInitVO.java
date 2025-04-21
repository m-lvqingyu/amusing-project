package com.amusing.start.oss.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MinioPartUploadInitVO {

    private String uploadId;

    private String objectName;

    private List<String> uploadUrlList;

}
