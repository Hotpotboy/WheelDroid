package com.yundongse.fragement;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.yundongse.R;
import com.yundongse.bean.BaiduNetBean;
import com.yundongse.bean.BaseNetBean;
import com.yundongse.bean.UserBean;
import com.yundongse.network.CommonNetTask;
import com.yundongse.network.impl.BaiduNetCallBackImpl;
import com.yundongse.util.ConstConfig;
import com.yundongse.util.PerferenceUtil;

public class MapFragment extends Fragment {
	public static final int UPDATE_USER_COO = 101;//更新用户坐标
	private View mRootView;
	
	MapView mMapView = null;//地图视图
	BaiduMap mBaiduMap;//百度地图
	
	MyLocationData locData;//我的位置
    LocationClient	mLocClient;
    MyLocationListener myListener = new MyLocationListener();//位置定位更新监听器
    boolean isFirstLocation = true;//是否第一次定位
    
    CommonNetTask netTask ;
    MapHandler handler = new MapHandler();
    
    private HashMap<Integer,UserBean> mUsers;
    private Vector<UserBean> mNeedUpdateUsers;//需要更新的用户
    
    public class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) return;
			MyLocationData locData = new MyLocationData.Builder()
								.accuracy(location.getRadius())
								// 此处设置开发者获取到的方向信息，顺时针0-360
								.direction(100).latitude(location.getLatitude())
								.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			saveLocationInfo(location);//保存坐标信息
			Log.i("坐标信息", location.getLatitude()+","+location.getLongitude());
			if(isFirstLocation){
				isFirstLocation=false;
			    LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
			    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			    mBaiduMap.animateMapStatus(u);
			}
			getOthersInfo();//搜寻附近好友
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			
		}
    	
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.activity_map, container, false);
		}
		mUsers = new HashMap<Integer,UserBean>();
		mNeedUpdateUsers = new Vector<UserBean>();
		
		mMapView = (MapView)mRootView.findViewById(R.id.bmapsView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通类型地图
		mBaiduMap.setMyLocationEnabled(true);//开启定位图层
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.NORMAL, true, null));
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){
			@Override
			public boolean onMarkerClick(Marker arg0) {
				Toast.makeText(getActivity(), arg0.getTitle(), Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		
		//默认缩放级别
		MapStatus ms = new MapStatus.Builder().zoom(17.0f).build();
		MapStatusUpdate msUpdate = MapStatusUpdateFactory.newMapStatus(ms);
		mBaiduMap.setMapStatus(msUpdate);
		// 定位初始化
	    mLocClient = new LocationClient(getActivity().getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(30000);//每隔三十秒定位一次
		mLocClient.setLocOption(option);
		mLocClient.start();		
		return mRootView;
	}

	@Override
	public void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
	}
	
	//保存地址信息
	private void saveLocationInfo(BDLocation location){
		PerferenceUtil.setStringValue(getActivity(), PerferenceUtil.CON_LATITUDE, String.valueOf(location.getLatitude()));
		PerferenceUtil.setStringValue(getActivity(), PerferenceUtil.CON_LONGITUDE, String.valueOf(location.getLongitude()));
	}
	
	//获取方圆1000米内的好友信息
	private void getOthersInfo(){
		netTask = new CommonNetTask(new BaiduNetCallBackImpl(){
			@Override
			protected void dealBean(BaseNetBean bean) throws JSONException {
				// TODO Auto-generated method stub
				if(bean.getStatus()!=0){//访问异常
					
				}else{
					JSONArray content = ((BaiduNetBean)bean).getContent();
					for(int i=0;i<content.length();i++){
						boolean isUpdate = false;//是否更新
						JSONObject object = (JSONObject)content.get(i);
						UserBean user;
						int userId = object.getInt("userId");
						if(mUsers.containsKey(userId)) user = mUsers.get(userId);
						else {
							user = new UserBean();
							user.setUserId(userId);
							user.setIsFriend(false);//默认不是好友
						}
						
						String title = object.getString("title");
						if(!TextUtils.isEmpty(title)&&!title.equals(user.getTitle())){
							user.setTitle(title);
							isUpdate=true;
						}
						
						int distance = object.getInt("distance");
						if(distance!=user.getDistance()){
							user.setDistance(distance);
							isUpdate=true;
						}
						
						JSONArray locationArray = object.getJSONArray("location");
						double[] location = new double[2];
						if(locationArray!=null&&locationArray.length()==2){
							location[0]=locationArray.getDouble(1);
							location[1]=locationArray.getDouble(0);
							if(!location.equals(user.getLocation())){
								user.setLocation(location);
								isUpdate=true;
							}
						}
						if(isUpdate) mNeedUpdateUsers.add(user);
					}
                    //更新视图
					handler.obtainMessage(UPDATE_USER_COO).sendToTarget();
				}
			}
			
		});//设置回调方法
		Map map = netTask.getPreParamBaiduMapApi(getActivity());
		String location = PerferenceUtil.getStringValue(getActivity(), PerferenceUtil.CON_LONGITUDE)+","+PerferenceUtil.getStringValue(getActivity(), PerferenceUtil.CON_LATITUDE);
		map.put("location", location);
		map.put("q", "");
		netTask.execute(ConstConfig.SERCHE_FRIENDS,null,map);
	}
	//更新坐标方法
	private void handleUpdateUserCoo(){
		if(this.mNeedUpdateUsers.size()>0){
			for(int i=0;i<mNeedUpdateUsers.size();i++){
				UserBean bean = mNeedUpdateUsers.get(i);
				//定义Maker坐标点  
				LatLng point = new LatLng(bean.getLocation()[0], bean.getLocation()[1]);  
				BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);  
				//构建MarkerOption，用于在地图上添加Marker  
				OverlayOptions option = new MarkerOptions().position(point).icon(bitmap).title("123");
				//在地图上添加Marker，并显示  
				Marker mark =(Marker)mBaiduMap.addOverlay(option);
				mark.setTitle(bean.getTitle());
			}
		}
	}
	
	public class MapHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
	         switch(msg.what){
	             case UPDATE_USER_COO:
	            	 handleUpdateUserCoo();
	            	 break;
	         }
		}
	}
}
