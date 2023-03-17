package com.example.yin.config;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 上传至阿里云OSS的方法
 * @author: qw Lu
 * @date: 2023/03/15 11:00:29
 **/
@Data
@Component
public class OssConfigurationProperties {

    // bucket
	@Value("${ali.bucketName}")
    private String bucketName;

    // 域名 oss-cn-chengdu.aliyuncs.com
	@Value("${ali.endpoint}")
    private String endpoint;

    // accessKeyId
	@Value("${ali.accessKey}")
    private String accessKey;

    // accessKeySecret
	@Value("${ali.accessKeySecret}")
    private String accessKeySecret;

	private String accessKeyid;


    // 文件上传
    public boolean upload(String filePath, String url) {
        OSSClient client = new OSSClient(endpoint, accessKey, accessKeySecret);
        try {
            // 上传文件
            InputStream inputStream = new FileInputStream(filePath);
            client.putObject(bucketName, url, inputStream);//上传文件
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            client.shutdown();
        }
    }
    // 重载函数的upload,为了接收不同的参数
    public boolean upload(InputStream is, String url) {
        OSSClient client = new OSSClient(endpoint, accessKey, accessKeySecret);
        try {
            // 上传文件流
            client.putObject(bucketName, url, is);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            client.shutdown();
        }
    }


    // 删除文件
    public boolean delFile(String path) {
        OSSClient client = new OSSClient(endpoint, accessKey, accessKeySecret);
        String[] split = path.split("/");
        String[] newPath = Arrays.copyOfRange(split, 3, split.length);
        StringBuilder ossPath = new StringBuilder();
        for (int i = 0; i < newPath.length; i++) {
            if (i == newPath.length - 1)
                ossPath.append(newPath[i]);
            else
                ossPath.append(newPath[i]).append("/");
        }
        System.out.println("path"+ossPath.toString());
        try {
            client.deleteObject(bucketName, ossPath.toString());
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            client.shutdown();
        }
    }


    // 获取文件路径
    public String getFileOSSUrl(String url) {
        String prefix = "https://";
        return prefix + bucketName + "." + endpoint.substring(7) + "/" +url;
    }
    
    //获取某个文件夹下的文件
    public List<OSSObjectSummary> getFileList(int num, String filePath){
        OSSClient client = new OSSClient(endpoint, accessKey,accessKeySecret);
        List<OSSObjectSummary> sums = new ArrayList<>();
        try {
            ObjectListing objectListing = client.listObjects(new ListObjectsRequest(bucketName)
            		.withMaxKeys(num).withPrefix(filePath));//.withMaxKeys(num)
            sums = objectListing.getObjectSummaries();
            for (OSSObjectSummary s : sums) {
                System.out.println("\t" + s.getKey());
            }
        } catch (OSSException oe) {
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code: " + oe.getErrorCode());
            System.out.println("Request ID: " + oe.getRequestId());
            System.out.println("Host ID: " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Error Message: " + ce.getMessage());
        } finally {
            client.shutdown();
        }
        return sums;
    }
}
