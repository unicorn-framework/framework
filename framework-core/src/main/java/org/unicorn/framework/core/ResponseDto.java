package org.unicorn.framework.core;

/**
 * 
 * @author xiebin
 *
 */
public class ResponseDto<T> {
     private String resCode;
     private T data;
     private String resInfo;
     private String url;
     
     public ResponseDto(){
    	 this.resCode=SysCode.SUCCESS.getCode();
    	 this.resInfo=SysCode.SUCCESS.getInfo();
     }
     public ResponseDto(T data){
    	 this.resCode=SysCode.SUCCESS.getCode();
    	 this.data=data;
    	 this.resInfo=SysCode.SUCCESS.getInfo();
     }
     
     public ResponseDto(ResBean resCode){
    	 this.resCode=resCode.getCode();
    	 this.resInfo=resCode.getInfo();
     }
     public ResponseDto(ResBean resCode,T data){
    	 this.resCode=resCode.getCode();
    	 this.data=data;
    	 this.resInfo=resCode.getInfo();
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
     
     public boolean isSuccess(){
    	 return this.getResCode().equals(SysCode.SUCCESS.getCode());
     }
}
