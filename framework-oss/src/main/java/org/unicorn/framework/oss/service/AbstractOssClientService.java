package org.unicorn.framework.oss.service;

import com.aliyun.oss.model.ObjectMetadata;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.oss.dto.OssUpload;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author xiebin
 */
public abstract class AbstractOssClientService {
    /**
     * oss上传文件名
     *
     * @param ossUpload
     * @return
     */
    public String getOssFileName(OssUpload ossUpload) {
        StringBuilder filePath = new StringBuilder(ossUpload.getStoragePrefix());
        filePath.append("/").append(ossUpload.isOverwriteFileName() ? ossUpload.getFileName() : String.valueOf(UUID.randomUUID()));
        if (!ossUpload.isOverwriteFileName()) {
            filePath.append(".").append(getFileExtension(ossUpload.getFileName()));
        }

        return filePath.toString();
    }


    /**
     * 获取文件的扩张名
     *
     * @param fileName
     * @return
     */
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public String getContentType(String fileName) {
        String fileExtension = getFileExtension(fileName);
        if ("png".equalsIgnoreCase(fileExtension)) {
            return "image/png";
        }

        if ("bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)
                || "png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if ("html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if ("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if ("xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        if ("mp4".equalsIgnoreCase(fileExtension)) {
            return "video/mp4";
        }
        return "text/html";
    }


    public abstract String fileUpload(InputStream inputStream, OssUpload ossUpload) throws PendingException;


    public ObjectMetadata metadata(OssUpload ossUpload) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setCacheControl("no-cache");
        metadata.setHeader("Pragma", "no-cache");
        metadata.setContentEncoding("utf-8");
        String contentType = getContentType(ossUpload.getFileName());
        metadata.setContentType(contentType);
        return metadata;
    }
}
