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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.jsonobject.JsonConcern;
import com.quanzi.table.QuanziTable;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：NewFollowerActivity  
 * 类描述：新的追随者页面
 * @author zhangshuai
 * @date 创建时间：2015-4-30 下午2:12:50 
 *
 */
public class NewFollowerActivity extends BaseActivity {

	/***********VIEWS************/
	private ListView myQuanziListView;
	private TextView leftTextView;// 导航栏左侧文字
	private View leftButton;// 导航栏左侧按钮
	List<JsonConcern> jsonConcernList;
	private UserPreference userPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_quanzi);

		userPreference = BaseApplication.getInstance().getUserPreference();
		jsonConcernList = new ArrayList<JsonConcern>();

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
		leftTextView.setText("新的追随者");
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
	 * 	网络获取新的追随者
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
					} else {
						jsonConcernList = FastJsonTool.getObjectList(response, JsonConcern.class);
						if (jsonConcernList != null && jsonConcernList.size() > 0) {
							myQuanziListView.setAdapter(new QuaiziAdapter());
						}
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("获取列表失败");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
			}
		};
		AsyncHttpClientTool.post(NewFollowerActivity.this, "quanzi/getMyNewFollowers", params, responseHandler);

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
			public ImageView headImageView;
			public TextView nameTextView;
			public CheckBox concernBtn;
			public TextView timeTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonConcernList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return jsonConcernList.get(position);
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
			final JsonConcern jsonConcern = jsonConcernList.get(position);
			if (jsonConcern == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(NewFollowerActivity.this).inflate(R.layout.new_follower_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.concernBtn = (CheckBox) view.findViewById(R.id.concern_btn);
				holder.timeTextView = (TextView) view.findViewById(R.id.time);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (jsonConcern.getUser_id() == userPreference.getU_id()) {
						Intent intent = new Intent(NewFollowerActivity.this, MyPersonDetailActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					} else {
						Intent intent = new Intent(NewFollowerActivity.this, PersonDetailActivity.class);
						intent.putExtra(UserTable.U_ID, jsonConcern.getUser_id());
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}

				}
			});

			imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonConcern.getUser_small_avatar()), holder.headImageView,
					ImageLoaderTool.getHeadImageOptions(0));

			holder.nameTextView.setText(jsonConcern.getUser_name());
			holder.timeTextView.setText(DateTimeTools.getMonAndDay(jsonConcern.getConcern_date()));

			// 显示关注
			if (jsonConcern.getUser_id() != userPreference.getU_id()) {
				holder.concernBtn.setVisibility(View.VISIBLE);
				holder.concernBtn.setChecked(jsonConcern.isIs_concern());
				holder.concernBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						RequestParams params = new RequestParams();
						params.put(QuanziTable.C_BECONCERNED_USERID, jsonConcern.getUser_id());
						params.put(QuanziTable.C_USERID, userPreference.getU_id());

						TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								super.onStart();
								if (!jsonConcern.isIs_concern()) {// 没有关注过
									jsonConcern.setIs_concern(true);
								} else {// 已经关注了
									jsonConcern.setIs_concern(false);
								}
							}

							@Override
							public void onSuccess(int statusCode, Header[] headers, String response) {
								// TODO Auto-generated method stub
								if (statusCode == 200) {
									if (response.equals("1")) {
										LogTool.i("关注成功！");
									} else if (response.equals("-2")) {
										LogTool.i("已经关注过了！");
									} else {
										LogTool.e("学校帖子", "关注返回错误" + response);
									}
								}
							}

							@Override
							public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
								// TODO Auto-generated method stub
								LogTool.e("学校帖子", "关注失败");
							}

							@Override
							public void onFinish() {
								// TODO Auto-generated method stub
								super.onFinish();
							}

						};
						if (!jsonConcern.isIs_concern()) {
							AsyncHttpClientTool.post(NewFollowerActivity.this, "quanzi/concern", params, responseHandler);
						} else {
							AsyncHttpClientTool.post(NewFollowerActivity.this, "quanzi/undoConcern", params, responseHandler);
						}
					}
				});
			} else {
				holder.concernBtn.setVisibility(View.GONE);
			}

			return view;
		}
	}

}
