package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
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

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.jsonobject.JsonQuanZiItem;
import com.quanzi.table.IndustryTable;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：MyQuanziActivity  
 * 类描述：我的圈子列表
 * @author zhangshuai
 * @date 创建时间：2015-4-30 上午11:28:23 
 *
 */
public class MyQuanziActivity extends BaseActivity {
	/***********VIEWS************/
	private ListView myQuanziListView;
	private TextView leftTextView;//导航栏左侧文字
	private View leftButton;//导航栏左侧按钮
	private List<JsonQuanZiItem> quanZiItemList;
	private UserPreference userPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_quanzi);
		userPreference = BaseApplication.getInstance().getUserPreference();
		quanZiItemList = new ArrayList<JsonQuanZiItem>();

		findViewById();
		initView();

		getUserList();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		myQuanziListView = (ListView) findViewById(R.id.myquanzi_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText("我的圈子");
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});

		myQuanziListView.setAdapter(new QuaiziAdapter());
	}

	/**
	 * 	网络获取我的圈子列表
	 */
	private void getUserList() {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("正在加载...");

		RequestParams params = new RequestParams();
		params.put(UserTable.U_ID, userPreference.getU_id());

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				dialog.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					if (response.equals("-1")) {
						LogTool.e("我的圈子返回错误" + response);
					} else {
						quanZiItemList = FastJsonTool.getObjectList(response, JsonQuanZiItem.class);
						if (quanZiItemList != null && quanZiItemList.size() > 0) {
							myQuanziListView.setAdapter(new QuaiziAdapter());
						}
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("我的圈子的列表失败");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
			}
		};
		AsyncHttpClientTool.post(MyQuanziActivity.this, "quanzi/myQuanzi", params, responseHandler);

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
			return quanZiItemList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return quanZiItemList.get(position);
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
			final ViewHolder holder;
			final JsonQuanZiItem jsonQuanZiItem = quanZiItemList.get(position);
			if (jsonQuanZiItem != null) {
				if (convertView == null) {
					view = LayoutInflater.from(MyQuanziActivity.this).inflate(R.layout.simple_list_item, null);
					holder = new ViewHolder();
					holder.nameTextView = (TextView) view.findViewById(R.id.name);
					holder.countTextView = (TextView) view.findViewById(R.id.count);
					view.setTag(holder); // 给View添加一个格外的数据 
				} else {
					holder = (ViewHolder) view.getTag(); // 把数据取出来  
				}
				holder.nameTextView.setText(jsonQuanZiItem.getI_name());
				holder.countTextView.setText("" + jsonQuanZiItem.getAmount());
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MyQuanziActivity.this, MyPeopleActivity.class);
					intent.putExtra(MyPeopleActivity.TYPE, MyPeopleActivity.MYQUANZI);
					intent.putExtra(IndustryTable.I_NAME, jsonQuanZiItem.getI_name());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});
			return view;
		}
	}
}
