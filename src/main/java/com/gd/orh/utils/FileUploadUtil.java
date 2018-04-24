package com.gd.orh.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUploadUtil {

    private static File getRootPath() {
        try {
            // 获取Windows下项目的根路径
            File rootDirectory = new File(ResourceUtils.getURL("classpath:").getPath());
            // 根路径不存在，获取Linux下项目的根路径
            if (!rootDirectory.exists()) {
                rootDirectory = new File("");
            }

            return rootDirectory;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("无法找到根目录路径");
        }
    }

    public static boolean upload(MultipartFile file, String relativePath, String filename) {
        // 创建相对路径下的子目录
        File uploadDirectory =
                new File(getRootPath().getAbsolutePath(),relativePath);

        if (!uploadDirectory.exists()) uploadDirectory.mkdirs();

        // 保存文件
        try {
            file.transferTo(new File(uploadDirectory, filename));
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
