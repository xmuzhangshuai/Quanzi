package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
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

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.jsonobject.JsonConcern;
import com.quanzi.table.IndustryTable;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：MyPeopleActivity  
 * 类描述：我的人脈圈或者我的追隨者页面
 * @author zhangshuai
 * @date 创建时间：2015-7-23 上午9:44:32 
 *
 */
public class MyPeopleActivity extends BaseActivity implements OnClickListener {

	/***********VIEWS************/
	public static final String TYPE = "type";//
	public static final String MYQUANZI = "myquanzi";//我的圈子
	public static final String MYFOLLOWER = "myfollower";//我的追随者

	private ListView allFavorListView;
	private TextView leftTextView;//导航栏左侧文字
	private View leftButton;//导航栏左侧按钮
	List<JsonConcern> jsonConcernList;
	private String industry_name;
	int pa_userid;
	String type = "";
	private UserPreference userPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_all_favors);
		userPreference = BaseApplication.getInstance().getUserPreference();
		type = getIntent().getStringExtra(TYPE);
		industry_name = getIntent().getStringExtra(IndustryTable.I_NAME);
		jsonConcernList = new ArrayList<JsonConcern>();

		if (type != null && !type.isEmpty()) {
			getUserList();
		}

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
		if (industry_name != null) {
			leftTextView.setText("" + industry_name);
		}
		leftButton.setOnClickListener(this);
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
	 * 	网络获取User信息
	 */
	private void getUserList() {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("正在加载...");

		RequestParams params = new RequestParams();
		params.put(UserTable.U_ID, userPreference.getU_id());
		if (type.equals(MYQUANZI) || type.equals(MYFOLLOWER)) {
			params.put(IndustryTable.I_NAME, industry_name);
		}

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
							allFavorListView.setAdapter(new MyFavorsAdapter());
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
		if (type.equals(MYQUANZI) && industry_name != null) {
			AsyncHttpClientTool.post(MyPeopleActivity.this, "quanzi/getQuanziUserListByIndustry", params,
					responseHandler);
		} else if (type.equals(MYFOLLOWER)) {
			AsyncHttpClientTool.post(MyPeopleActivity.this, "quanzi/getFollowersByIndustryID", params, responseHandler);
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
				view = LayoutInflater.from(MyPeopleActivity.this).inflate(R.layout.followers_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}
			//
			//			view.setOnClickListener(new OnClickListener() {
			//
			//				@Override
			//				public void onClick(View v) {
			//					// TODO Auto-generated method stub
			//					Intent intent = new Intent(MyPeopleActivity.this, PersonDetailActivity.class);
			//					intent.putExtra(PersonDetailActivity.JSONUSER, jsonUser);
			//					startActivity(intent);
			//					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			//				}
			//			});

			imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonConcern.getUser_small_avatar()),
					holder.headImageView, ImageLoaderTool.getHeadImageOptions(0));
			holder.nameTextView.setText(jsonConcern.getUser_name());

			return view;
		}
	}
}
