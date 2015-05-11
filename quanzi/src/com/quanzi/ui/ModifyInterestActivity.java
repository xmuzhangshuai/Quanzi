package com.quanzi.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;

/**
 *
 * 项目名称：quanzi  
 * 类名称：ModifyInterestActivity  
 * 类描述：修改兴趣爱好
 * @author zhangshuai
 * @date 创建时间：2015-4-30 下午6:46:51 
 *
 */
public class ModifyInterestActivity extends BaseActivity {

	/***********VIEWS************/
	private ListView industryListView;
	private TextView leftTextView;//导航栏左侧文字
	private View leftButton;//导航栏左侧按钮

	private String[] industry = new String[] { "电影", "运动", "音乐", "美食", "购物", "摄影", "游泳" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_interest);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		industryListView = (ListView) findViewById(R.id.myquanzi_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("兴趣爱好");
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});

		industryListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,
				industry));

	}
}
