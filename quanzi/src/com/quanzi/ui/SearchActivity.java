package com.quanzi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseActivity;

/**
 *
 * 项目名称：quanzi  
 * 类名称：SearchActivity  
 * 类描述：搜索界面
 * @author zhangshuai
 * @date 创建时间：2015-4-30 上午9:31:58 
 *
 */
public class SearchActivity extends BaseActivity implements OnClickListener {
	private ListView searchOutputListView;
	private TextView searchBtn;
	private View backBtn;

	private String[] names = new String[] { "张帅", "刘伟强", "黄蓉发", "王坤" };
	private int[] headimages = new int[] { R.drawable.act_type, R.drawable.balcklist, R.drawable.arrows, R.drawable.btn_blue_normal_shape };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		searchOutputListView = (ListView) findViewById(R.id.search_output_list);
		searchBtn = (TextView) findViewById(R.id.search_btn);
		backBtn = findViewById(R.id.left_btn_bg);

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		searchBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		searchOutputListView.setAdapter(new MyOutputAdapter());
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
			overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
			break;
		case R.id.search_btn:
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
	private class MyOutputAdapter extends BaseAdapter {
		private class ViewHolder {
			public ImageView headImageView;
			public TextView nameTextView;
			public CheckBox concernBtn;
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
				view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.followers_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.concernBtn = (CheckBox) view.findViewById(R.id.concern_btn);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}

			holder.headImageView.setImageResource(headimages[position]);
			holder.nameTextView.setText(name);
			holder.concernBtn.setVisibility(View.VISIBLE);

			return view;
		}
	}
}
