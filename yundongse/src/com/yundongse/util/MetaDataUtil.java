package com.yundongse.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author zhanghang 百度地图相关key(meta-data获取)
 */
public class MetaDataUtil {
	private Context context;
	
	public final  String API_KEY_NAME = "com.baidu.lbsapi.API_KEY";
	public final  String SERVER_KEY_NAME = "com.baidu.lbsapi.SERVER_KEY";
	
	public MetaDataUtil(Context c){
		context = c;
	}
	
	public  String getMetaDataValue(String key) {
		ApplicationInfo info;
		try {
			info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return info.metaData.getString(key);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
}
