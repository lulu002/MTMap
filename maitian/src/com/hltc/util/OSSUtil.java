package com.hltc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.hltc.common.GlobalConstant;

public class OSSUtil {
	
       
    // 上传文件
    public static void uploadFile(String bucketName, String key, String filename)
            throws OSSException, ClientException, FileNotFoundException {
        File file = new File(filename);

        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("image/jpeg");

        InputStream input = new FileInputStream(file);
        PutObjectResult result = new OSSClient(GlobalConstant.ALIYUN_ACCESSKEY_ID, GlobalConstant.ALIYUN_ACCESSKEY_SECRET)
        									.putObject(bucketName, key, input, objectMeta);
    }
    
    
} 