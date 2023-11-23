package com.amusing.start.platform.service.ding.impl;

import cn.hutool.json.JSONUtil;
import com.amusing.start.code.CommCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.platform.constant.DingTalkConstant;
import com.amusing.start.platform.constant.DingTalkErrorCode;
import com.amusing.start.platform.enums.DingTalkFileType;
import com.amusing.start.platform.service.ding.IDingTalkUploadService;
import com.amusing.start.platform.service.ding.IDingTalkUserService;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.google.common.base.Throwables;
import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lv.QingYu
 * @since 2023/10/17
 */
@Slf4j
@Service
public class DingTalkUploadServiceImpl implements IDingTalkUploadService {

    private final IDingTalkUserService dingTalkUserService;

    @Autowired
    public DingTalkUploadServiceImpl(IDingTalkUserService dingTalkUserService) {
        this.dingTalkUserService = dingTalkUserService;
    }

    @Override
    public String uploadImage(MultipartFile file) {
        return doUpload(file, DingTalkFileType.IMAGE);
    }

    @Override
    public String uploadVoice(MultipartFile file) {
        return doUpload(file, DingTalkFileType.VOICE);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return doUpload(file, DingTalkFileType.FILE);
    }

    @Override
    public String uploadVideo(MultipartFile file) {
        return doUpload(file, DingTalkFileType.VIDEO);
    }

    private String doUpload(MultipartFile file, DingTalkFileType fileType) {
        DingTalkClient client = new DefaultDingTalkClient(DingTalkConstant.UPLOAD_PATH);
        OapiMediaUploadRequest req = new OapiMediaUploadRequest();
        req.setType(fileType.name().toLowerCase());
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            log.error("[DoUpload]-err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        String name = file.getName();
        String contentType = file.getContentType();
        FileItem item = new FileItem(name, inputStream, contentType);
        req.setMedia(item);
        String accessToken = dingTalkUserService.getDingTalkAccessToken().getAccessToken();
        OapiMediaUploadResponse response = null;
        try {
            response = client.execute(req, accessToken);
        } catch (ApiException e) {
            log.error("[DoUpload]-err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        log.info("[DoUpload]-result:{}", JSONUtil.toJsonStr(response));
        if (DingTalkErrorCode.SUCCESS != response.getErrcode()) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        return response.getMediaId();
    }

}
