package com.yundongse.bean;

public class UserBean {
    private String title;//�û���
    private int userId;//�û�ID
    private double[] location;//λ��
    private int distance;//�뵱ǰ�û��ľ���
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
