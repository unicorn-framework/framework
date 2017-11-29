package org.unicorn.framework.mybatis.dto;

import java.io.Serializable;

/**
 * 
 * @author xiebin
 *
 */

public class AbstractPageDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pageNo;
    private int pageSize;
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
    
	
}
