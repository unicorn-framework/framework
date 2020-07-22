//package org.unicorn.framework.gateway.dto;
//
//import io.swagger.annotations.ApiModel;
//import lombok.*;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.unicorn.framework.core.SysCode;
//import org.unicorn.framework.core.dto.AbstractRequestDto;
//import org.unicorn.framework.core.exception.PendingException;
//
///**
// * @author xiebin
// */
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@EqualsAndHashCode(callSuper = false)
//@ApiModel(value = "基础请求头", description = "基础请求头")
//@Slf4j
//public class BaseSecurityDto extends AbstractRequestDto {
//    private static final long serialVersionUID = 1L;
//    /**
//     * appKey 由服务端分配
//     */
//    private String appKey;
//    /**
//     * 请求时间戳
//     */
//    private String timestamp;
//
//    /**
//     * 随机字符串
//     */
//    private String nonceStr;
//
//    /**
//     * 签名
//     */
//    private String sign;
//
//
//    @Override
//    public void vaildatioinThrowException() throws PendingException {
//        if (StringUtils.isBlank(appKey)) {
//            log.warn("appKey为空");
//            throw new PendingException(SysCode.API_SECURITY_ERROR);
//        }
//        if (StringUtils.isBlank(timestamp)) {
//            log.warn("timestamp为空");
//            throw new PendingException(SysCode.API_SECURITY_ERROR);
//        }
//        if (StringUtils.isBlank(nonceStr)) {
//            log.warn("nonceStr为空");
//            throw new PendingException(SysCode.API_SECURITY_ERROR);
//        }
//        if (StringUtils.isBlank(sign)) {
//            log.warn("sign为空");
//            throw new PendingException(SysCode.API_SECURITY_ERROR);
//        }
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sbuilder = new StringBuilder();
//        sbuilder.append("appKey=" + appKey);
//        sbuilder.append("&timestamp=" + timestamp);
//        sbuilder.append("&nonceStr=" + nonceStr);
//        sbuilder.append("&appKey=" + appKey);
//        return sbuilder.toString();
//    }
//}
