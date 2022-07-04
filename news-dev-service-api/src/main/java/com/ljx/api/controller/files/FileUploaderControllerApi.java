package com.ljx.api.controller.files;


import com.ljx.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "文件上传的controller", tags = {"文件上传的controller"})
@RequestMapping("fs")
public interface FileUploaderControllerApi {
    @ApiOperation(value = "上传用户头像", notes = "上传用户头像", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public GraceJSONResult uploadFace(@RequestParam String userId,
                                      MultipartFile file) throws Exception;

    @ApiOperation(value = "发文章上传多个图片", notes = "发文章上传多个图片", httpMethod = "POST")
    @PostMapping("/uploadSomeFiles")
    public GraceJSONResult uploadSomeFiles(@RequestParam String userId,
                                      MultipartFile[] files) throws Exception;


}
