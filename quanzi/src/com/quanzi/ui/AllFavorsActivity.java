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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.jsonobject.JsonUser;
import com.quanzi.table.ActivityTable;
import com.quanzi.table.PostTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.LogTool;

/**
 *
 * 项目名称：quanzi  
 * 类名称：AllFavorsActivity  
 * 类描述：查看点赞者的页面
 * @author zhangshuai
 * @date 创建时间：2015-4-28 下午10:18:17 
 *
 */
public class AllFavorsActivity extends BaseActivity implements OnClickListener {
	/***********VIEWS************/
	public static final String PA_ID = "pa_id";
	public static final String PA_USERID = "pa_user_id";
	public static final String FAVOR_COUNT = "favor_count";
	public static final String TYPE = "type";

	private ListView allFavorListView;
	private TextView leftTextView;//导航栏左侧文字
	private View leftButton;//导航栏左侧按钮
	List<JsonUser> jsonUserList;
	int pa_id;
	int pa_userid;
	int favor_count;
	String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_all_favors);
		pa_id = getIntent().getIntExtra(PA_ID, 0);
		pa_userid = getIntent().getIntExtra(PA_USERID, 0);
		favor_count = getIntent().getIntExtra(FAVOR_COUNT, 0);
		type = getIntent().getStringExtra(TYPE);

		jsonUserList = new ArrayList<JsonUser>();

		if (pa_id > 0 && pa_userid > 0) {
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
		leftTextView.setText("" + favor_count + "赞");
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
		if (type.equals("post")) {
			params.put(PostTable.P_POSTID, pa_id);
			params.put(PostTable.P_USERID, pa_userid);
		} else if (type.equals("act")) {
			params.put(ActivityTable.A_ACTID, pa_id);
			params.put(ActivityTable.A_USERID, pa_userid);
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
						LogTool.e("赞列表返回错误" + response);
					} else {
						jsonUserList = FastJsonTool.getObjectList(response, JsonUser.class);
						if (jsonUserList != null && jsonUserList.size() > 0) {
							allFavorListView.setAdapter(new MyFavorsAdapter());
						}
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("获取赞的列表失败");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
			}
		};
		if (type.equals("act")) {
			AsyncHttpClientTool.post(AllFavorsActivity.this, "activity/getLikeUserList", params, responseHandler);
		} else if (type.equals("post")) {
			AsyncHttpClientTool.post(AllFavorsActivity.this, "post/getLikeUserList", params, responseHandler);
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
			return jsonUserList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return jsonUserList.get(position);
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
			final JsonUser jsonUser = jsonUserList.get(position);
			if (jsonUser == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(AllFavorsActivity.this).inflate(R.layout.followers_list_item, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) view.findViewById(R.id.head_image);
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(AllFavorsActivity.this, PersonDetailActivity.class);
					intent.putExtra(PersonDetailActivity.JSONUSER, jsonUser);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});

			imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonUser.getU_small_avatar()),
					holder.headImageView, ImageLoaderTool.getHeadImageOptions(0));
			holder.nameTextView.setText(jsonUser.getU_nickname());

			return view;
		}
	}

}
