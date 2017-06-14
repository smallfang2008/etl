package com.zbiti.common.dao;

import java.util.HashMap;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import com.zbiti.common.vo.DataVO;
import com.zbiti.core.dto.Page;

public class DaoUtil
{
	@SuppressWarnings("all")
	public static Map<String, Object> toMap(Object bean)
	{
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy/MM/dd/ HH:mm:ss"}) );
		if (bean instanceof java.util.Map) { return (Map<String, Object>) (bean); }
		if (bean instanceof DataVO) { return ((DataVO) bean).getData(); }
		Map<String, Object> data = (Map<String, Object>) JSONObject.fromObject(bean);
		Map<String, Object> newData = new HashMap<String, Object>();
		for (String key : data.keySet())
		{
			Object value = data.get(key);
			if (value instanceof net.sf.json.JSONNull)
				continue;

			newData.put(key, value);

		}

		return newData;
	}

	@SuppressWarnings("all")
	public static Map<String, Object> toMap(Object bean, Page page)
	{
		Map vo = toMap(bean);
		vo.put("page", page);
		return vo;
	}
}
