package com.pinyougou.upload.controller;

import com.pinyougou.common.entity.Image;
import com.pinyougou.common.util.FastDFSClient;
import com.pinyougou.entity.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author silent
 * @version 1.0
 * @date 2019/6/30 20:29
 **/
@RestController
@RequestMapping("/upload")
public class UploadController {
    /**
     * 支持跨域 只有这个两个的跨域请求上传图片才可以被允许
     */
    @RequestMapping("/deleteFile")
    @CrossOrigin(origins = {"http://localhost:9102", "http://localhost:9101"}, allowCredentials = "true")
    public Result delete(@RequestBody List<Image> filePath) {
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastdfs_client.conf");
            fastDFSClient.deleteFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
        return new Result(true, "删除成功");
    }

    @RequestMapping("/uploadFile")
    @CrossOrigin(origins = {"http://localhost:9102", "http://localhost:9101"}, allowCredentials = "true")
    public Result upload(@RequestParam(value = "file") MultipartFile file) {
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastdfs_client.conf");
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String path = fastDFSClient.uploadFile(bytes, extName);
            String realPath = "http://192.168.111.135/" + path;
            return new Result(true, realPath);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }

}
