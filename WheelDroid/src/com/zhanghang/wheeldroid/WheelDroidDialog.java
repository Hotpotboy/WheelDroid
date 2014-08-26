package com.zhanghang.wheeldroid;

import java.util.ArrayList;

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

	private WheelDroidView wheelView;
	private WheelDroidAdapter adapter;

	private static ArrayList<String> list;
	static {
		list = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			list.add("item " + (i + 1));
		}
	}

	public WheelDroidDialog(Context c) {
		context = c;
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View dialogLayout = layoutInflater.inflate(R.layout.wheel_dialog, null);
		wheelView = (WheelDroidView) dialogLayout.findViewById(R.id.wheelView);
		adapter = new WheelDroidAdapter(context, list);
		wheelView.setAdapter(adapter);

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
				String text = wheelView.getCurItemValue();
				Toast.makeText(context, text, Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
		});
	}

}
