package com.highplace.biz.pm.service.util.cloud;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.Date;

public class AliyunOssHelper implements OssHelperInterface {

    public static final Logger logger = LoggerFactory.getLogger(AliyunOssHelper.class);

    private OSSClient ossClient;

    private JSONObject succesJSONObject;
    private JSONObject errorJSONObject;

    public AliyunOssHelper(String endpoint, String accessKeyId, String accessKeySecret) {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        succesJSONObject = new JSONObject();
        succesJSONObject.put("code", 0);
        succesJSONObject.put("message", "SUCCESS");
        errorJSONObject = new JSONObject();
        errorJSONObject.put("code", -1);
    }

    private JSONObject getErrorJSONObject(String errMsg) {
        errorJSONObject.put("message", errMsg);
        return errorJSONObject;
    }

    private JSONObject getSuccesJSONObject() {
        return succesJSONObject;
    }

    //释放cos客户端
    public void releaseCosClient() {

        // 关闭释放资源
        ossClient.shutdown();
    }

    //上传文件到cos
    //返回格式:{"code":0,"message":"SUCCESS"}
    public JSONObject uploadFile(String bucketName, String cosFilePath, String localFilePath) {

        if (cosFilePath.startsWith("/")) cosFilePath = cosFilePath.substring(1);
        try {
            ossClient.putObject(bucketName, cosFilePath, new File(localFilePath));
            return getSuccesJSONObject();
        } catch (Exception e) {
            logger.error("aliyun upload file error:{}" ,e.getMessage());
            e.printStackTrace();
            return getErrorJSONObject(e.getMessage());
        }
    }

    //下载文件到本地
    //返回格式:{"code":0,"message":"SUCCESS"}
    public JSONObject getFile(String bucketName, String cosFilePath, String localFilePath) {

        if (cosFilePath.startsWith("/")) cosFilePath = cosFilePath.substring(1);
        try {
            ossClient.getObject(new GetObjectRequest(bucketName, cosFilePath), new File(localFilePath));
            return getSuccesJSONObject();
        } catch (Exception e) {
            logger.error("aliyun download file error:{}" ,e.getMessage());
            e.printStackTrace();
            return getErrorJSONObject(e.getMessage());
        }
    }

    //删除cos远程文件
    //返回格式:{"code":0,"message":"SUCCESS","request_id":"NTllYWJhOGVfY2NhMzNiMGFfYWViOF9lNTc4YWM="}
    public JSONObject deleteFile(String bucketName, String cosFilePath) {

        if (cosFilePath.startsWith("/")) cosFilePath = cosFilePath.substring(1);
        try {
            ossClient.deleteObject(bucketName, cosFilePath);
            return getSuccesJSONObject();
        } catch (Exception e) {
            logger.error("aliyun delete file error:{}" ,e.getMessage());
            e.printStackTrace();
            return getErrorJSONObject(e.getMessage());
        }
    }

    //创建目录，目录必须以/结尾，不能以/开头,如"sample_folder/"
    public void createFolder(String bucketName, String cosFolderPath) {

        if (cosFolderPath.startsWith("/")) cosFolderPath = cosFolderPath.substring(1);
        try {
            ossClient.putObject(bucketName, cosFolderPath, new ByteArrayInputStream(new byte[0]));
        } catch (Exception e) {
            logger.error("aliyun create folder error:{}" ,e.getMessage());
            e.printStackTrace();
        }
    }

    //生成文件下载URL,文件名不能以/结尾,如：pic/test.jpg
    public String getDownLoadUrl(String bucketName, String cosFilePath, String noUse) {

        if (cosFilePath.startsWith("/")) cosFilePath = cosFilePath.substring(1);
        // 设置URL过期时间为1小时
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        URL url = ossClient.generatePresignedUrl(bucketName, cosFilePath, expiration);

        //logger.debug("oss replace1:" + StringUtils.replace(url.toString(),aliyunConfig.getEndpoint(),aliyunConfig.getEndpointWan()));

        //默认是内网域名,需要替换成外网域名,去掉url中的"-internal"
        return (url == null) ? null : StringUtils.replaceOnce(url.toString(), "-internal", "");
    }
}
