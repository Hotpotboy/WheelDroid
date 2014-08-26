package com.zhanghang.wheeldroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView show;
	private WheelDroidDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		show=(TextView)findViewById(R.id.show);
		show.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				dialog = new WheelDroidDialog(MainActivity.this);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
