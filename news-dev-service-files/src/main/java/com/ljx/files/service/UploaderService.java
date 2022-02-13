package com.ljx.files.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploaderService {

    /**
     * TODO使用fastdfs上传文件
     */
    //public String uploadFdfs(MultipartFile file, String fileExtName) throws Exception;

    /**
     * 使用OSS上传文件
     */
    public String uploadOSS(MultipartFile file,
                            String userId,
                            String fileExtName) throws Exception;

}
