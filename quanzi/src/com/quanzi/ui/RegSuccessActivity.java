package com.quanzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;

/**
 *
 * 项目名称：quanquan  
 * 类名称：RegSuccessActivity  
 * 类描述：注册成功页面
 * @author zhangshuai
 * @date 创建时间：2015-4-9 下午8:57:14 
 *
 */
public class RegSuccessActivity extends BaseActivity {
	// UI references.
	private Button beginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reg_success);
		
		findViewById();
		initView();
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		beginButton = (Button) findViewById(R.id.begin);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		beginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegSuccessActivity.this, MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}

}
