package org.unicorn.framework.oss.dto;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.dto.AbstractRequestDto;
import org.unicorn.framework.core.exception.PendingException;

/**
 * oss存储服务Dto类
 *
 * @author xiebin
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OssUpload extends AbstractRequestDto {

    private static final long serialVersionUID = -1;

    /**
     * 文件URL
     */
    private String url;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件上传存储路径前缀 由调用方自行制定    如   heads
     */
    private String storagePrefix = "";
    /** --  -- **/
    private boolean overwriteFileName;

    @Override
    public void vaildatioinThrowException() throws PendingException {
        if (StringUtils.isBlank(fileName)) {
            throw new PendingException(SysCode.PARA_NULL, "文件名不能为空");
        }
        if (StringUtils.isBlank(url)) {
            throw new PendingException(SysCode.PARA_NULL, "文件URL不能为空");
        }
        if (StringUtils.isBlank(storagePrefix)) {
            throw new PendingException(SysCode.PARA_NULL, "存储路径不能为空");
        }

    }
}
