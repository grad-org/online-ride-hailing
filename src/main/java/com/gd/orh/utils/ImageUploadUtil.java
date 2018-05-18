package com.gd.orh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadUtil.class);

    private static File ROOT_PATH = null;

    static {
        try {
            if (new File(ResourceUtils.getURL("classpath:").getPath()).exists()) {
                ROOT_PATH = new File("E:/static/");
            } else {
                ROOT_PATH = new File("/root/static/");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isImageEmpty(String image) {
        return StringUtils.isEmpty(image);
    }

    public static boolean isImageNotEmpty(String image) {
        return !isImageEmpty(image);
    }

    public static String getImageContent(String image) {
        String[] a = image.split("base64,");

        return a.length == 2 ? a[1] : null;
    }

    public static InputStream getInputStreamFromImageContent(String imageContent) {
        byte[] decodeFromString = Base64Utils.decodeFromString(imageContent);

        return new ByteArrayInputStream(decodeFromString);
    }

    public static boolean uploadImageToRootPath(String relativePath, String fileName, InputStream in, String formatName) {
        boolean result = false;
        try {
            File rootPath = ROOT_PATH;
            File uploadPath = new File(rootPath.getAbsolutePath(), relativePath);

            if (!uploadPath.exists())
                uploadPath.mkdirs();

            BufferedImage src = ImageIO.read(in);
            File target = new File(uploadPath, fileName);

            result = ImageIO.write(src, formatName, target);
        } catch (IOException e) {
            LOGGER.error("图片上传失败", e);
            throw new RuntimeException(e);
        }
        return result;
    }
}
