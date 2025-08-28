package com.baizhanshopping.shopping_file_service.service;

import com.alibaba.nacos.shaded.com.google.common.io.ByteArrayDataInput;
import com.baizhanshopping.shopping_common.result.BusException;
import com.baizhanshopping.shopping_common.result.CodeEnum;
import com.baizhanshopping.shopping_common.service.FileService;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;

@DubboService
public class FileServiceImpl implements FileService {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Value("${fdfs.fileUrl}")
    private String fileUrl;// Nginx访问FastDFS中文件的路径
    @Override
    public String uploadImage(byte[] fileBytes, String fileName) {
        if(fileBytes.length!=0){
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
                //获取文件后缀名
                String fileSuffix = fileName.substring(fileName.lastIndexOf(".")+1);
                StorePath storePath = fastFileStorageClient.uploadFile(inputStream, inputStream.available(), fileSuffix, null);
                String imageUrl=fileUrl+"/"+storePath.getFullPath();
                return imageUrl;
            }catch (Exception e){
                throw new BusException(CodeEnum.UPLOAD_FILE_ERROR);
            }
        }else {
            throw new BusException(CodeEnum.UPLOAD_FILE_ERROR);
        }
    }

    @Override
    public void delete(String filePath) {
        fastFileStorageClient.deleteFile(filePath);
    }
}
