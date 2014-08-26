package com.zhanghang.wheeldroid;

import java.util.List;

import android.content.Context;

public class WheelDroidAdapter {
	
	private List mList;
	private Context context;
	
	public WheelDroidAdapter(Context c,List l){
		mList=l;
		context = c;
	}
	
	public int getCount(){
		return mList.size();
	}
	
	public Object getItem(int index){
		return mList.get(index);
	}

}
