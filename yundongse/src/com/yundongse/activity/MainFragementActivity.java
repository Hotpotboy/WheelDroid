package com.yundongse.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.baidu.mapapi.SDKInitializer;
import com.yundongse.R;
import com.yundongse.adpter.TabGridApdater;
import com.yundongse.fragement.CommunityFragment;
import com.yundongse.fragement.MapFragment;
import com.yundongse.fragement.MatchFragment;
import com.yundongse.fragement.MyFragment;
import com.yundongse.util.ConstConfig;

public class MainFragementActivity extends FragmentActivity implements OnItemClickListener {
	private Class[] tabClass = {MatchFragment.class,MapFragment.class,CommunityFragment.class,MyFragment.class};
	private MapFragment mapFragment;
	private MatchFragment matchFragment;
	private CommunityFragment communityFragment;
	private MyFragment myFragment;
	
	private GridView tab_menu;
	private int mNowTabIndex=-1;
	
	private TabGridApdater apdter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
        SDKInitializer.initialize(getApplicationContext());//≥ı ºªØµÿÕº
		
		tab_menu=(GridView)findViewById(R.id.tab_menu);
		tab_menu.setOnItemClickListener(this);
		apdter = new TabGridApdater(this);
		tab_menu.setAdapter(apdter);
		showTable(0);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		showTable(arg2);
	}
	
	private void showTable(int index){
		FragmentManager manager = this.getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		matchFragment = (MatchFragment)manager.findFragmentByTag(ConstConfig.tabName[0]);
		if(matchFragment!=null){
			transaction.hide(matchFragment);
		}
		
		mapFragment = (MapFragment)manager.findFragmentByTag(ConstConfig.tabName[1]);
		if(mapFragment!=null){
			transaction.hide(mapFragment);
		}
		
		communityFragment = (CommunityFragment)manager.findFragmentByTag(ConstConfig.tabName[2]);
		if(communityFragment!=null){
			transaction.hide(communityFragment);
		}
		
		myFragment = (MyFragment)manager.findFragmentByTag(ConstConfig.tabName[3]);
		if(myFragment!=null){
			transaction.hide(myFragment);
		}
			
		mNowTabIndex = index;
		switch(index){
		    case 0:
		    	if(matchFragment == null){
		    		matchFragment=new MatchFragment();
					transaction.add(R.id.tab_fragement, matchFragment,ConstConfig.tabName[0]);
				}
		    	transaction.show(matchFragment);
			    break;
		    case 1:
		    	if(mapFragment == null){
					mapFragment=new MapFragment();
					transaction.add(R.id.tab_fragement, mapFragment,ConstConfig.tabName[1]);
		    	}
				transaction.show(mapFragment);
		    	break;
		    case 2:
		    	if(communityFragment == null){
		    		communityFragment=new CommunityFragment();
					transaction.add(R.id.tab_fragement, communityFragment,ConstConfig.tabName[2]);
				}
		    	transaction.show(communityFragment);
		    	break;
		    case 3:
		    	if(myFragment == null){
		    		myFragment=new MyFragment();
					transaction.add(R.id.tab_fragement, myFragment,ConstConfig.tabName[3]);
				}
		    	transaction.show(myFragment);
		    	break;
		}
		transaction.commit();
	}

}
