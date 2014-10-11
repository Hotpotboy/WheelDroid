package com.yundongse.bean;

import org.json.JSONArray;

public class BaiduNetBean extends BaseNetBean{
    private int total;
    private int size;
    private JSONArray content;
    
    public void setTotal(int t){
    	total = t;
    }
    public int getTotal(){
    	return total;
    }
    
    public void setSize(int s){
    	size = s;
    }
    
    public int getSize(){
    	return size;
    }
    
    public void setContent(JSONArray c){
    	content = c;
    }
    
    public JSONArray getContent(){
    	return content;
    }
}
