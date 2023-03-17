package com.example.yin.config;

import com.example.yin.common.R;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class DoUpload {
    @Resource OssConfigurationProperties ossConfigurationProperties;
    //上传到oss服务器
    public R OssUpload(MultipartFile File, String UrlPath){
        boolean IsUpload = false;
        try {
            IsUpload = ossConfigurationProperties.upload(File.getInputStream(), UrlPath);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        if (!IsUpload) {// 上传oss服务器失败
            return R.fatal("上传失败");
        }
        // 保存链接到数据库
        return R.success("上传成功");
    }
}
