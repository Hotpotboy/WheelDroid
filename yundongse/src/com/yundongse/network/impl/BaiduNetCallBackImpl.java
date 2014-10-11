package com.yundongse.network.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yundongse.bean.BaiduNetBean;
import com.yundongse.bean.BaseNetBean;

public abstract  class BaiduNetCallBackImpl extends NetCallBackImpl {

	@Override
	protected BaseNetBean parseJSONOject(JSONObject object)
			throws JSONException {
		BaiduNetBean bean = new BaiduNetBean();
		int status = object.getInt("status");
		bean.setStatus(status);
		int total = object.getInt("total");
		bean.setTotal(total);
		int size = object.getInt("size");
		bean.setSize(size);
		JSONArray content = (JSONArray)object.getJSONArray("contents");
		bean.setContent(content);
		return bean;
	}
     
}
 