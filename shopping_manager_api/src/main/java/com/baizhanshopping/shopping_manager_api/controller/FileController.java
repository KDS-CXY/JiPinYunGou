package com.baizhanshopping.shopping_manager_api.controller;

import com.baizhanshopping.shopping_common.result.BaseRsult;
import com.baizhanshopping.shopping_common.service.FileService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    @DubboReference
    private FileService fileService;

    /**
     * 上传文件
     * @param file 文件
     * @return 文件路径
     * @throws IOException
     */
    @PostMapping("/uploadImage")
    public BaseRsult<String> upload(MultipartFile file) throws IOException {
        // MultipartFile对象不能再服务间传递，必须转为byte数组
        byte[] bytes = file.getBytes();
        String url = fileService.uploadImage(bytes, file.getOriginalFilename());
        return BaseRsult.success(url);
    }

    /**
     * 删除文件
     * @param filePath 文件路径
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public BaseRsult delete(String filePath){
        fileService.delete(filePath);
        return BaseRsult.success();
    }
}
