package com.yundongse.adpter;

import com.yundongse.R;
import com.yundongse.R.id;
import com.yundongse.R.layout;
import com.yundongse.util.ConstConfig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TabGridApdater extends BaseAdapter {
	private Context context;
	
	public TabGridApdater(Context c){
		context = c;
	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return ConstConfig.tabName[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHold hold;
        if(arg1==null){
        	hold = new ViewHold();
        	LayoutInflater layout = LayoutInflater.from(context) ;
        	arg1 = layout.inflate(R.layout.item_tab, null);
        	hold.tab_icon = (ImageView)arg1.findViewById(R.id.tab_icon);
        	hold.tab_name = (TextView)arg1.findViewById(R.id.tab_name);
        	arg1.setTag(hold);
        }else{
        	hold = (ViewHold)arg1.getTag();
        }
        hold.tab_icon.setImageDrawable(context.getResources().getDrawable(ConstConfig.tabIcon[arg0]));
        hold.tab_name.setText(ConstConfig.tabName[arg0]);
		return arg1;
	}
	
	class ViewHold{
		TextView tab_name;
		ImageView tab_icon;
	}

}
