package com.yundongse.fragement;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.yundongse.R;

public class MatchFragment extends  Fragment {
	private View mRootView;
	private TranslateAnimation[] mAnimations;//item移动动画
	
	private RelativeLayout item1,item2,item3;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		if(mRootView==null)
			mRootView = inflater.inflate(R.layout.activity_match, container,false);
		item1 = (RelativeLayout)mRootView.findViewById(R.id.layout_1);
		item2 = (RelativeLayout)mRootView.findViewById(R.id.layout_2);
		item3 = (RelativeLayout)mRootView.findViewById(R.id.layout_3);
		
		//初始化动画数组
		mAnimations = new TranslateAnimation[3];
		initAnimationArray();
		item1.setAnimation(mAnimations[0]);
		mAnimations[0].startNow();
		item2.setAnimation(mAnimations[1]);
		mAnimations[1].startNow();
		item3.setAnimation(mAnimations[2]);
		mAnimations[2].startNow();
		return mRootView;
	}
	
	//初始化动画数组元素
	private void initAnimationArray(){
		for(int i=0;i<mAnimations.length;i++){
			if(mAnimations[i]==null){
				TranslateAnimation element = new TranslateAnimation(Animation.ABSOLUTE,0, Animation.ABSOLUTE,0, Animation.RELATIVE_TO_PARENT,0, Animation.RELATIVE_TO_PARENT,1);
				int time = 5+(int)(3*Math.random());//随机秒数
				element.setDuration(time*1000);
				element.setFillAfter(true);
				mAnimations[i]=element;
			}
		}
	}

}
