package com.ljx.files.controller;

import com.ljx.api.controller.files.FileUploaderControllerApi;
import com.ljx.files.resource.FileResource;
import com.ljx.files.service.UploaderService;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.grace.result.ResponseStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FileUploaderController implements FileUploaderControllerApi {

    final static Logger logger = LoggerFactory.getLogger(FileUploaderController.class);
    @Autowired
    private UploaderService uploaderService;
    @Autowired
    private FileResource fileResource;
    @Override
    public GraceJSONResult uploadFace(String userId,
                                      MultipartFile file) throws Exception {
        String path = "";
        //判断file是否为空
        if (file != null) {
            //获得文件上传的名称
            String fileName = file.getOriginalFilename();
            //判断文件名不为空
            if (StringUtils.isNotBlank(fileName)) {
                String[] fileNameArr = fileName.split("\\.");
                //获得文件后缀
                String suffix = fileNameArr[fileNameArr.length - 1];
                //判断后缀是否合法
                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")
                ) {
                    return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }
                //开始文件上传,得到文件路径
                path = uploaderService.uploadOSS(file,userId,suffix);

            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
            }
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }

        String finalPath = "";
        if (StringUtils.isNotBlank(path)) {
            finalPath = fileResource.getOssHost() + path;
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        //打印日志
        logger.info("path = " + finalPath);
        //return GraceJSONResult.ok(finalPath);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult uploadSomeFiles(String userId,
                                           MultipartFile[] files) throws Exception {
        //声明list，用户存放多个图片的地址路径，返回到前端
        List<String> imageUrlList = new ArrayList<>();
        if (files != null && files.length > 0) {
            //遍历multipart文件
            for (MultipartFile file : files) {
                String path = "";
                //判断file是否为空
                if (file != null) {
                    //获得文件上传的名称
                    String fileName = file.getOriginalFilename();
                    //判断文件名不为空
                    if (StringUtils.isNotBlank(fileName)) {
                        String[] fileNameArr = fileName.split("\\.");
                        //获得文件后缀
                        String suffix = fileNameArr[fileNameArr.length - 1];
                        //判断后缀是否合法
                        if (!suffix.equalsIgnoreCase("png") &&
                                !suffix.equalsIgnoreCase("jpg") &&
                                !suffix.equalsIgnoreCase("jpeg")
                        ) {
                            //return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                            //单个文件上传错误，不抛出
                            continue;
                        }
                        //开始文件上传,得到文件路径
                        path = uploaderService.uploadOSS(file,userId,suffix);

                    } else {
                        //return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
                        //文件名错误，直接continue
                        continue;
                    }
                } else {
                    //return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
                    continue;
                }

                String finalPath = "";
                if (StringUtils.isNotBlank(path)) {
                    finalPath = fileResource.getOssHost() + path;
                    imageUrlList.add(finalPath);
                    logger.info("path = " + finalPath);
                } else {
                    continue;
                }
            }
        }
        //return GraceJSONResult.ok(imageUrlList);
        return GraceJSONResult.ok();

    }
}
