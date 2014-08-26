package com.zhanghang.wheeldroid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class WheelDroidDialog {
	private Context context;

	private Dialog dialog;
	private Button button;

	private WheelDroidView wheelView1,wheelView2;
	private WheelDroidAdapter adapter,adapter1;
	
	private static  Calendar calendar;

	private static ArrayList<String> list;
	private static ArrayList<String> timeList;
	static {
		
		list = new ArrayList<String>();
		for (int i = 0; i < 7; i++) {
			String item = "";
			if(i==0) item = "����";
			else if(i==1) item = "����";
			else{
				calendar=Calendar.getInstance();
				calendar.add(GregorianCalendar.DAY_OF_MONTH, i);
				String week = calendar.get(Calendar.DAY_OF_WEEK)+"";
				if("7".equals(week)) week="��";
				item=(calendar.get(Calendar.MONTH)+1)+"��"+calendar.get(Calendar.DAY_OF_MONTH)+"(����"+week+")";
			}
			list.add(item);
		}
	}

	public WheelDroidDialog(Context c) {
		context = c;
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View dialogLayout = layoutInflater.inflate(R.layout.wheel_dialog, null);
		wheelView1 = (WheelDroidView) dialogLayout.findViewById(R.id.wheelView1);
		adapter = new WheelDroidAdapter(context, list);
		wheelView1.setAdapter(adapter);
		wheelView2 = (WheelDroidView) dialogLayout.findViewById(R.id.wheelView2);
		getTimeList(1);
		adapter1 = new WheelDroidAdapter(context, timeList);
		wheelView2.setAdapter(adapter1);
		wheelView1.setOnWheelViewChangedListener(new IOnWheelViewChangedListener(){

			@Override
			public boolean onChanged(int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String item = wheelView1.getAdapter().getItem(newValue)+"";
				if(item.endsWith("6)")||item.endsWith("��)")){
					getTimeList(2);
				}else if(item.equals("����")){
					getTimeList(1);
				}else getTimeList(0);
				//����������
				adapter1 = new WheelDroidAdapter(context, timeList);
				wheelView2.setAdapter(adapter1);
				wheelView2.setCurItem(0);
				return false;
			}
			
		});
		

		// ��ʼ���Ի���
		dialog = new Dialog(context, R.style.wheel_dialog);
		dialog.setContentView(dialogLayout);// ����������ͼ
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_anim);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.width = ((Activity) context).getWindowManager().getDefaultDisplay()
				.getWidth();
		window.setGravity(Gravity.BOTTOM);// ����λ��Ϊ�׶�
		dialog.onWindowAttributesChanged(wl);
		dialog.setCanceledOnTouchOutside(true);// ����Ի��������ط��Ƿ�ʹ�Ի�����ʧ~
		dialog.show();

		// ȷ����ť
		button = (Button) dialogLayout.findViewById(R.id.sure);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String text = wheelView1.getCurItemValue()+" "+wheelView2.getCurItemValue();
				Toast.makeText(context, text, Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
		});
	}
	
	private static void getTimeList(int type){
		timeList = new ArrayList<String>();
		if(type==0){//8�㵽18��
			for(int i =8;i<=18;i++){
				String item = i+":00";
				timeList.add(item);
			}
		}else if(type == 1){//��ǰʱ�䵽18��
			calendar = Calendar.getInstance();
			int i = calendar.get(Calendar.HOUR_OF_DAY);
			if(i>18) timeList.add("�޺���ʱ��");
			else{
				for(;i<=18;i++){
					String item = i+":00";
					timeList.add(item);
				}
			}
		}else if(type==2){//10�㵽18��
			for(int i =10;i<=16;i++){
				String item = i+":00";
				timeList.add(item);
			}
		}
	}

}
