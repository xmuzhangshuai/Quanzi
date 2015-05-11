package com.quanzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;

/**
 *
 * 项目名称：quanzi  
 * 类名称：MyFollowerActivity  
 * 类描述：我的追随者
 * @author zhangshuai
 * @date 创建时间：2015-4-30 下午2:00:35 
 *
 */
public class MyFollowerActivity extends BaseActivity {
	/***********VIEWS************/
	private ListView myQuanziListView;
	private TextView leftTextView;//导航栏左侧文字
	private View leftButton;//导航栏左侧按钮
	private View newFollowerBtn;//新的追随者

	private String[] industry = new String[] { "保险", "程序开发", "保险", "教育", "互联网", "摄影", "医疗" };
	private int[] counts = new int[] { 12, 34, 45, 26, 345, 234, 4 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_follower);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		myQuanziListView = (ListView) findViewById(R.id.myquanzi_list);
		newFollowerBtn = findViewById(R.id.follower);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("我的追随者");
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		newFollowerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyFollowerActivity.this, NewFollowerActivity.class));
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		myQuanziListView.setAdapter(new QuaiziAdapter());
	}

	/**
	 *
	 * 项目名称：quanzi  
	 * 类名称：MyFavorsAdapter  
	 * 类描述：
	 * @author zhangshuai
	 * @date 创建时间：2015-4-29 下午2:37:23 
	 *
	 */
	private class QuaiziAdapter extends BaseAdapter {
		private class ViewHolder {
			public TextView nameTextView;
			public TextView countTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return industry.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return industry[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			final String name = industry[position];
			if (name == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(MyFollowerActivity.this).inflate(R.layout.simple_list_item, null);
				holder = new ViewHolder();
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.countTextView = (TextView) view.findViewById(R.id.count);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			holder.nameTextView.setText(name);
			holder.countTextView.setText("" + counts[position]);

			return view;
		}
	}

}
