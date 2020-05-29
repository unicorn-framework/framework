package org.unicorn.framework.oss.service;

import com.aliyun.oss.OSSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.oss.config.OssBucketConfig;
import org.unicorn.framework.oss.config.OssPartproperties;
import org.unicorn.framework.oss.dto.OssUpload;

import java.io.InputStream;

/**
 * @author xiebin
 */
@Service
@Slf4j
public class OssPartUploadService extends AbstractOssClientService {
    @Autowired
    private OssBucketConfig ossBucketConfig;
    @Autowired
    private OssPartproperties ossPartproperties;
    @Autowired
    private OSSClient ossClient;


    @Override
    public String fileUpload(InputStream inputStream, OssUpload ossUpload) throws PendingException {
        try {


            return null;
        } catch (Exception e) {
            throw new PendingException(SysCode.SYS_FAIL, "文件上传失败", e);
        } catch (Throwable te) {
            throw new PendingException(SysCode.SYS_FAIL, "文件上传失败", te);
        }

    }
}
