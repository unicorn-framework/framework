package org.unicorn.framework.core.dto;

import java.io.Serializable;

/**
 * 
 * @author xiebin
 *
 */

public abstract class AbstractRequestPageDto extends AbstractRequestDto{

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
