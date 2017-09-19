package com.yuntu.page.support.jpa;

import com.yuntu.common.utils.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Created by jack on 3/7/17.
 * 自定义page对象
 */
public class PageRequestCustom implements Serializable{
	public PageRequestCustom() {
	}

	public PageRequestCustom(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public PageRequestCustom(Sort.Direction direction, int page, int size, String sortProperties) {
		this.direction = direction;
		this.page = page;
		this.size = size;
		this.sortProperties = sortProperties;
	}

	//	@ApiModelProperty("当前页0开始")
	private int page=0;
//	@ApiModelProperty("当前页的大小")
	private int size=10;
//	@ApiModelProperty("排序方向")
	private Sort.Direction direction;
//	@ApiModelProperty("根据哪个属性进行排序")
	private String sortProperties;

	public String getSortProperties() {
		return sortProperties;
	}

	public void setSortProperties(String sortProperties) {
		this.sortProperties = sortProperties;
	}

	public Sort.Direction getDirection() {
		return direction;
	}

	public void setDirection(Sort.Direction direction) {
		this.direction = direction;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public PageRequest getPageRequest() {
		if(StringUtils.isNotBlank(sortProperties)&&direction!=null) {
			return new PageRequest(page, size, direction, sortProperties);
		}else {
			return new PageRequest(page,size);
		}
	}
	@Override
	public String toString() {
		return "PageRequestCustom{" +
				"direction=" + direction +
				", page=" + page +
				", size=" + size +
				", sortProperties='" + sortProperties + '\'' +
				'}';
	}
}
