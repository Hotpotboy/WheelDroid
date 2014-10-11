package com.yundongse.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.yundongse.network.impl.NetCallBackImpl;
import com.yundongse.network.interfaces.ICallBack;
import com.yundongse.util.ConstConfig;
import com.yundongse.util.MetaDataUtil;

import android.content.Context;
import android.os.AsyncTask;

public class CommonNetTask extends AsyncTask<Object, Void, Void> {
	
	private ICallBack callBack;//回调接口
	
	public CommonNetTask(ICallBack c){
		callBack = c;
	}

	@Override
	protected Void doInBackground(Object... params) {
		// TODO Auto-generated method stub
		BaseHttpClient httpClient = new BaseHttpClient();
		try{
			String url = params[0].toString();//url地址
			List postParam =(List)params[1];
			Map getParam = (Map)params[2];
			
			//返回结果
			HttpResponse response=null;
			if(postParam!=null){
				response = httpClient.doPost(url, postParam);
			}else if(getParam!=null){
				response = httpClient.doGet(url, getParam);
			}
			
			if(response!=null){
				callBack.callBack(response);
			}
			
		}catch(ArrayIndexOutOfBoundsException e){
			
		}catch(ClientProtocolException e){
			
		}catch(JSONException e){
			
		}catch(IOException e){
			
		}
		return null;
	}
	
	//获取百度地图预定参数
	public Map getPreParamBaiduMapApi(Context context){
		HashMap map = new HashMap();
		MetaDataUtil metaData = new MetaDataUtil(context);
		map.put("ak",metaData.getMetaDataValue(metaData.SERVER_KEY_NAME));
		map.put("geotable_id", ConstConfig.BAIDU_TABLE_ID);
		map.put("coord_type", ConstConfig.BAIDU_COORD_TYPE);
		map.put("radius", ConstConfig.BAIDU_RADIUS);
		return map;
	}

}
