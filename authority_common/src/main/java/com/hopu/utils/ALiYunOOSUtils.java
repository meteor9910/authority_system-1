package com.hopu.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ALiYunOOSUtils {
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private static String accessKeyId = "LTAI4G4AbVYBndRdhKR77AHA";
    private static String accessKeySecret = "xqzBJeFYUEFBbc0IDU6orRYlXAQIOP";

    public static void uploadFile(MultipartFile file , String fileName) throws IOException {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
        ossClient.putObject("as-img", fileName, file.getInputStream());

// 关闭OSSClient。
        ossClient.shutdown();
    }

    public static void deleteFile(String objectName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject("as-img", objectName);

// 关闭OSSClient。
        ossClient.shutdown();
    }

  public static String createAnOrderId() {
        DateFormat dateFormat01 = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat01.format(new java.util.Date());
    }

    public static void downLoadFile(String bucketName, String objectName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File("<yourLocalFile>"));

        // 关闭OSSClient。
        ossClient.shutdown();
    }

}
