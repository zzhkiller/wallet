package com.coezal.wallet.api.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 分页参数
 * @author yj
 */
public class PageDtoReq implements Serializable {

	private static final long serialVersionUID = 2404902482976922378L;
	
	@ApiModelProperty(name = "currentPage", value = "页数(第几页),默认第一页", notes = "页数(第几页),默认第一页")
	private  Integer currentPage = 1;
	@ApiModelProperty(name = "pageSize", value = "每页展示条数,默认10条", notes = "每页展示条数,默认10条")
	private Integer pageSize = 10;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
}
