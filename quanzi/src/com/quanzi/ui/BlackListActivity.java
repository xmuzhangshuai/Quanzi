package com.quanzi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;

/**
 *
 * 项目名称：quanzi  
 * 类名称：BlackListActivity  
 * 类描述：查看我的黑名单
 * @author zhangshuai
 * @date 创建时间：2015-4-30 下午7:43:18 
 *
 */
public class BlackListActivity extends BaseActivity implements OnClickListener {
	/***********VIEWS************/
	private ListView allFavorListView;
	private TextView leftTextView;//导航栏左侧文字
	private View leftButton;//导航栏左侧按钮

	private String[] names = new String[] { "张帅", "刘伟强", "黄蓉发", "王坤", "张帅", "刘伟强", "黄蓉发", "王坤", "张帅", "刘伟强", "黄蓉发",
			"王坤" };
	private int[] headimages = new int[] { R.drawable.headimage, R.drawable.headimage1, R.drawable.headimage2,
			R.drawable.headimage3, R.drawable.headimage4, R.drawable.headimage5, R.drawable.headimage6,
			R.drawable.headimage7, R.drawable.headimage, R.drawable.headimage1, R.drawable.headimage2,
			R.drawable.headimage3 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_all_favors);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		allFavorListView = (ListView) findViewById(R.id.allfovers_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("黑名单");
		leftButton.setOnClickListener(this);

		allFavorListView.setAdapter(new MyFavorsAdapter());
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;

		default:
			break;
		}
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
	private class MyFavorsAdapter extends BaseAdapter {
		private class ViewHolder {
			public ImageView headImageView;
			public TextView nameTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return names[position];
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
			final String name = names[position];
			if (name == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(BlackListActivity.this).inflate(R.layout.followers_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			holder.headImageView.setImageResource(headimages[position]);
			holder.nameTextView.setText(name);

			return view;
		}
	}
}
