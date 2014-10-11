package com.yundongse.util;

import com.yundongse.R;

public class ConstConfig {
	 //检索百度云地址
     public static final String SERCHE_FRIENDS = "http://api.map.baidu.com/geosearch/v3/nearby";
     
     public static final String BAIDU_TABLE_ID = "78616";//百度云表ID
     public static final String BAIDU_COORD_TYPE = "3";//百度坐标类型
     public static final String BAIDU_RADIUS = "1000";//百度地图云检索范围（米）
     
     //网络状态
     public static final int HTTP_STATUS_OK = 200;
     
     
     //程序
     public static final String[] tabName = {"打擂台","比武招‘亲’","龙门客栈","我的江湖"};
 	 public static final int[] tabIcon = {R.drawable.leitai,R.drawable.wu,R.drawable.kezhan,R.drawable.wo};
	 
}
