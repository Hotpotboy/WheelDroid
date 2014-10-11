package com.yundongse.bean;

public class UserBean {
    private String title;//用户名
    private int userId;//用户ID
    private double[] location;//位置
    private int distance;//与当前用户的距离
    private boolean isFriend;
    
    public void setTitle(String t){
    	title = t;
    }
    
    public String getTitle(){
    	return title;
    }
    
    public void setUserId(int u){
    	userId = u;
    }
    
    public int getUserId(){
    	return userId;
    }
    
    public void setLocation(double[] i){
    	location = i;
    }
    
    public double[] getLocation(){
    	return location;
    }
    
    public void setDistance(int d){
    	distance = d;
    }
    
    public int getDistance(){
    	return distance;
    }
    
    public void setIsFriend(boolean is){
    	isFriend = is;
    }
    
    public boolean getIsFriend(){
    	return isFriend;
    }
}
