package com.atguigu.ssyx.product.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.atguigu.ssyx.product.service.FileUploadService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-05 19:48
 * @Description:
 **/
@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${aliyun.endpoint}")
    private String endpoint;
    @Value("${aliyun.keyid}")
    private String accessKey;
    @Value("${aliyun.keysecret}")
    private String secreKey;
    @Value("${aliyun.bucketname}")
    private String bucketName;
    /**
     * 图片上传
     * @param file
     * @return
     */
    @Override
    public String fileUpload(MultipartFile file) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, secreKey);

        try {
            //输入流
            InputStream inputStream = file.getInputStream();
            //获取文件实际名称
            String objectName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            objectName = uuid+objectName;

            //对上传文件进行分组，根据当前的年月日 用joda-time依赖
            //objectName: 2023/10/10/uuid01.jpg
            String currentDateTime = new DateTime().toString("yyyy/MM/dd");
            objectName = currentDateTime+"-"+objectName;
            // 创建PutObjectRequest对象。
            //第一个bucket名称
            //第二个 上传文件路径+名称
            //比如只有名称 01.jpg
            //比如 /aa/bb/001.jpg
            //第三个 文件输入流
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            //设置该属性可以返回response，如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            //如果上传成功返回200
            System.out.println(result.getResponse().getStatusCode());
            System.out.println(result.getResponse().getErrorResponseAsString());
            System.out.println(result.getResponse().getUri());
            //获取上传图片的路径
            String url = result.getResponse().getUri();
            return url;
        } catch (IOException e) {
            System.out.println("Error Message:" + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }
}
