package com.quanzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.quanzi.R;

/**
 * �����ƣ�LoginOrRegisterActivity 
 * ��������ע����¼����ҳ�� 
 * �����ˣ� ��˧ 
 * ����ʱ�䣺2015-4-4 ����9:23:09
 * 
 */
public class LoginOrRegisterActivity extends com.quanzi.base.BaseActivity {
	private Button loginButton;
	private Button registerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_or_register);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		loginButton = (Button) findViewById(R.id.login_btn);
		registerButton = (Button) findViewById(R.id.register_btn);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginOrRegisterActivity.this, LoginActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginOrRegisterActivity.this, RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}
}
