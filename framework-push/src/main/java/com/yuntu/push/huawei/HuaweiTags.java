/**  **/
package com.yuntu.push.huawei;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author icewang
 * @date 2016年10月15日
 */
public class HuaweiTags implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Map<String, String[]>> tags;

	public List<Map<String, String[]>> getTags() {
		return tags;
	}

	public void setTags(List<Map<String, String[]>> tags) {
		this.tags = tags;
	}
	
	public HuaweiTags() {
		tags = Lists.newArrayList();
	}
	
	public HuaweiTags(Map<String, String[]> tags) {
		this();
		for(Entry<String, String[]> entry : tags.entrySet()) {
			Map<String, String[]> map = Maps.newHashMap();
			map.put(entry.getKey(), entry.getValue());
			this.tags.add(map);
		}
	}
}
