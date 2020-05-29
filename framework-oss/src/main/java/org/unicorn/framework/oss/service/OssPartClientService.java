package org.unicorn.framework.oss.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.PartETag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.oss.config.OssBucketConfig;
import org.unicorn.framework.oss.config.OssPartproperties;
import org.unicorn.framework.oss.dto.OssUpload;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author xiebin
 */
@Service
@Slf4j
public class OssPartClientService extends AbstractOssClientService {

    @Autowired
    private OssBucketConfig ossBucketConfig;

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private OssPartproperties ossPartproperties;

    @Autowired
    private ExecutorService executorService;

    /**
     * 初始化分块上传事件并生成uploadID，用来作为区分分块上传事件的唯一标识
     *
     * @return
     */
    protected String claimUploadId(String bucketName, String key) {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request);
        log.info(result.getUploadId());
        return result.getUploadId();
    }

    @Override
    public String fileUpload(InputStream inputStream, OssUpload ossUpload) throws PendingException {

        // 获取上传文件的名称，作为在OSS上的文件名
        String key = getOssFileName(ossUpload);
        try {
            String uploadId = claimUploadId(ossBucketConfig.getBucketName(), key);
            // 设置每块为 5M(除最后一个分块以外，其他的分块大小都要==5MB)
            final long partSize = ossPartproperties.getPartSize() * 1024 * 1024L;
            // 计算分块数目
            long fileLength = inputStream.available();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount++;
            }

            // 分块 号码的范围是1~10000。如果超出这个范围，OSS将返回InvalidArgument的错误码。
            if (partCount > ossPartproperties.getPartCount()) {
                throw new PendingException(SysCode.FILE_UPLOAD_TOO_BIG);
            } else {
                log.info("一共分了 " + partCount + " 块");
            }

            List<PartETag> partETags = Collections.synchronizedList(new ArrayList<PartETag>());

            CountDownLatch countDownLatch = new CountDownLatch(partCount);
            /**
             * 将分好的文件块加入到list集合中
             */
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;

                // 线程执行。将分好的文件块加入到list集合中
                executorService.execute(new AliyunOSSUploadThread(countDownLatch, partETags, ossClient, inputStream, startPos, curPartSize, i + 1, uploadId, key, ossBucketConfig.getBucketName()));
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*
             * 完成分块上传
             */
            completeMultipartUpload(partETags, key, uploadId);
            // 返回上传文件的URL地址
            return ossBucketConfig.getAccessPrefix() + "/" + key;
        } catch (Exception e) {
            log.error("上传失败！", e);
            throw new PendingException(SysCode.SYS_FAIL, "上传失败");
        }
    }


    /**
     * 将文件分块进行升序排序并执行文件上传。
     *
     * @param uploadId
     */
    protected void completeMultipartUpload(List<PartETag> partETags, String key, String uploadId) {
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(ossBucketConfig.getBucketName(),
                key, uploadId, partETags);
        // 完成分块上传
        ossClient.completeMultipartUpload(completeMultipartUploadRequest);
    }
}
