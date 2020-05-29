package org.unicorn.framework.oss.service;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.oss.config.OssBucketConfig;
import org.unicorn.framework.oss.dto.OssUpload;

import java.io.InputStream;


/**
 * @author xiebin
 */
@Service
public class OssClientService extends AbstractOssClientService {
    @Autowired
    private OssBucketConfig ossBucketConfig;

    @Autowired
    private OSSClient ossClient;

    @Override
    public String fileUpload(InputStream inputStream, OssUpload ossUpload) throws PendingException {
        try {
            String ossFileName = getOssFileName(ossUpload);
            // 上传文件
            ossClient.putObject(ossBucketConfig.getBucketName(), ossFileName, inputStream, metadata(ossUpload));
            return ossBucketConfig.getAccessPrefix() + "/" + ossFileName;
        } catch (Exception e) {

            throw new PendingException(SysCode.SYS_FAIL, "文件上传失败", e);
        }
    }
}
