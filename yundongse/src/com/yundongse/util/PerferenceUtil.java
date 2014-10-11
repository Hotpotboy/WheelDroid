package com.yundongse.util;


import android.content.Context;
import android.content.SharedPreferences;

public class PerferenceUtil {
	public static final String CON_LATITUDE = "latitude";
	public static final String CON_LONGITUDE = "Longitude";
	
	public static String getStringValue(Context context,String key){
		SharedPreferences shared = context.getSharedPreferences(key,Context.MODE_PRIVATE);
		return shared.getString(key, "");
	}
    
	public static void setStringValue(Context context,String key,String value){
		SharedPreferences shared = context.getSharedPreferences(key,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString(key, value);
		editor.commit();
	}
    
	
}
