package com.yundongse.network.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.yundongse.bean.BaseNetBean;
import com.yundongse.network.interfaces.ICallBack;
import com.yundongse.util.ConstConfig;

public abstract class NetCallBackImpl implements ICallBack {

	@Override
	public void callBack(HttpResponse response) throws JSONException {
		// TODO Auto-generated method stub
		int statusCode = response.getStatusLine().getStatusCode();
		switch(statusCode){
		   case ConstConfig.HTTP_STATUS_OK:
			   String content = getResponse(response.getEntity());//获取内容
			   JSONTokener jsonParser = new JSONTokener(content);
			   JSONObject jsonObject = (JSONObject)jsonParser.nextValue();
			   this.dealBean(parseJSONOject(jsonObject));	  
		   default:
			   break;
		}
	}

	private String getResponse(HttpEntity entity) {
		try {
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(entity.getContent(),HTTP.DEFAULT_CONTENT_CHARSET));
			String result = new String();
			String buffer;
			while((buffer=bufferIn.readLine())!=null){
				result+=buffer;
			}
			return result;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//解析方法
	protected abstract BaseNetBean parseJSONOject(JSONObject object) throws JSONException;
	
	//处理bean
	protected abstract void dealBean(BaseNetBean bean) throws JSONException;

}
