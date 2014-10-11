package com.yundongse.fragement;

import com.yundongse.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MatchFragment extends  Fragment {
	private View mRootView;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		if(mRootView==null)
			mRootView = inflater.inflate(R.layout.activity_match, container,false);
		
		return mRootView;
	}

}
