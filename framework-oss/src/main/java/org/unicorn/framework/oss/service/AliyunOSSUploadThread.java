package org.unicorn.framework.oss.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiebin
 */
@Slf4j
public class AliyunOSSUploadThread implements Runnable {
    private InputStream instream;
    private long startPos;

    private long partSize;
    private int partNumber;
    private String uploadId;
    private static String key;
    private static String bucketName;
    private OSSClient ossClient;
    private List<PartETag> partETags;
    private CountDownLatch countDownLatch;

    /**
     * 创建构造方法
     *
     * @param instream 要上传的文件流
     * @param startPos    每个文件块的开始
     * @param partSize
     * @param partNumber
     * @param uploadId    作为块的标识
     * @param key         上传到OSS后的文件名
     */
    public AliyunOSSUploadThread(CountDownLatch countDownLatch, List<PartETag> partETags, OSSClient ossClient, InputStream instream, long startPos, long partSize, int partNumber, String uploadId, String key, String bucketName) {
        this.ossClient = ossClient;
        this.instream = instream;
        this.startPos = startPos;
        this.partSize = partSize;
        this.partNumber = partNumber;
        this.uploadId = uploadId;
        AliyunOSSUploadThread.key = key;
        AliyunOSSUploadThread.bucketName = bucketName;
        this.partETags = partETags;
        this.countDownLatch = countDownLatch;
    }

    /**
     * 分块上传核心方法(将文件分成按照每个5M分成N个块，并加入到一个list集合中)
     */
    @Override
    public void run() {
        try {
            // 跳到每个分块的开头
            instream.skip(this.startPos);
            // 创建UploadPartRequest，上传分块
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(key);
            uploadPartRequest.setUploadId(this.uploadId);
            uploadPartRequest.setInputStream(instream);
            uploadPartRequest.setPartSize(this.partSize);
            uploadPartRequest.setPartNumber(this.partNumber);

            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
            log.info("Part#" + this.partNumber + " done\n");
            synchronized (partETags) {
                // 将返回的PartETag保存到List中。
                partETags.add(uploadPartResult.getPartETag());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
            if (instream != null) {
                try {
                    // 关闭文件流
                    instream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
