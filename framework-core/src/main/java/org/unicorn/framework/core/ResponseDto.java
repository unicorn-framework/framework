package org.unicorn.framework.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author xiebin
 */
@ApiModel(value = "返回对象", description = "返回对象")
public class ResponseDto<T> {
    @ApiModelProperty(value = "返回码", name = "返回码")
    private String resCode;
    private T data;
    @ApiModelProperty(value = "返回码描述信息", name = "返回码描述信息")
    private String resInfo;
    @ApiModelProperty(value = "请求URL", name = "请求URL")
    private String url;

    public ResponseDto() {
        this.resCode = SysCode.SUCCESS.getCode();
        this.resInfo = SysCode.SUCCESS.getInfo();
    }

    public ResponseDto(T data) {
        this.resCode = SysCode.SUCCESS.getCode();
        this.data = data;
        this.resInfo = SysCode.SUCCESS.getInfo();
    }

    public ResponseDto(ResBean resCode) {
        this.resCode = resCode.getCode();
        this.resInfo = resCode.getInfo();
    }

    public ResponseDto(ResBean resCode, T data) {
        this.resCode = resCode.getCode();
        this.data = data;
        this.resInfo = resCode.getInfo();
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getResInfo() {
        return resInfo;
    }

    public void setResInfo(String resInfo) {
        this.resInfo = resInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @ApiModelProperty(value = "请求是否成功", name = "请求是否成功")
    public boolean isSuccess() {
        return this.getResCode().equals(SysCode.SUCCESS.getCode());
    }
}
