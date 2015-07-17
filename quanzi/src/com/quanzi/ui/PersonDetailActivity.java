package com.quanzi.ui;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseFragmentActivity;
import com.quanzi.db.SchoolDbService;
import com.quanzi.jsonobject.JsonUser;
import com.quanzi.table.QuanziTable;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.ImageLoaderTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：PersonDetailActivity  
 * 类描述：他人的个人中心页面
 * @author zhangshuai
 * @date 创建时间：2015-5-8 下午3:08:37 
 *
 */
public class PersonDetailActivity extends BaseFragmentActivity implements OnClickListener {
	public static final String JSONUSER = "jsonuser";

	private TextView leftTextView;//导航栏左侧文字
	private View leftButton;//导航栏左侧按钮
	private View contactBtn;//私信按钮
	private View moreBtn;//更多按钮
	private CheckBox concernBtn;
	private View postBtn;
	private View dataBtn;
	private ImageView headImageView;
	private TextView basicInfo;
	private TextView introduce;
	private int index;
	private int currentTabIndex;
	private View[] mTabs;

	private Fragment[] fragments;
	private PersonDetailPostFragment personDetailPostFragment;
	private PersonDataFragment personDataFragment;

	private int userId;
	private String smallAvator;
	private String userName;
	private UserPreference userPreference;
	private JsonUser jsonUser;
	private boolean isConcerned = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_person_detail);
		userPreference = BaseApplication.getInstance().getUserPreference();
		jsonUser = (JsonUser) getIntent().getSerializableExtra(JSONUSER);
		userId = getIntent().getIntExtra(UserTable.U_ID, -1);
		userName = getIntent().getStringExtra(UserTable.U_NICKNAME);
		smallAvator = getIntent().getStringExtra(UserTable.U_SMALL_AVATAR);

		findViewById();
		initView();

		if (jsonUser == null) {
			imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(smallAvator), headImageView,
					ImageLoaderTool.getHeadImageOptions(10));
			getUser();//网络获取user数据
		} else {
			userId = jsonUser.getU_id();
			initPersonView();
		}
		getConcerned(userId);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		leftTextView = (TextView) findViewById(R.id.nav_text);
		leftButton = findViewById(R.id.left_btn_bg);
		contactBtn = findViewById(R.id.nav_right_btn2);
		moreBtn = findViewById(R.id.nav_right_btn1);
		headImageView = (ImageView) findViewById(R.id.head_image);
		basicInfo = (TextView) findViewById(R.id.basic_info);
		introduce = (TextView) findViewById(R.id.person_intro);
		postBtn = findViewById(R.id.postBtn);
		dataBtn = findViewById(R.id.dataBtn);
		concernBtn = (CheckBox) findViewById(R.id.concern_btn);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		leftTextView.setText(userName);
		leftButton.setOnClickListener(this);
		concernBtn.setOnClickListener(this);
		concernBtn.setVisibility(View.GONE);
	}

	/**
	 * 菜单显示
	 */
	void showMoreDialog() {

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		PersonDetailMoreDialogFragment newFragment = PersonDetailMoreDialogFragment.newInstance();
		newFragment.show(ft, "dialog");
	}

	/**
	 * 初始化个人信息
	 */
	private void initPersonView() {
		personDataFragment = new PersonDataFragment(jsonUser);
		personDetailPostFragment = new PersonDetailPostFragment(userId);
		fragments = new Fragment[] { personDetailPostFragment, personDataFragment };

		mTabs = new View[2];
		mTabs[0] = (View) findViewById(R.id.postBtn);
		mTabs[1] = (View) findViewById(R.id.dataBtn);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);

		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, personDetailPostFragment)
				.show(personDetailPostFragment).commit();

		postBtn.setOnClickListener(this);
		dataBtn.setOnClickListener(this);
		contactBtn.setOnClickListener(this);
		moreBtn.setOnClickListener(this);

		//设置头像
		if (!TextUtils.isEmpty(jsonUser.getU_small_avatar())) {
			imageLoader.displayImage(AsyncHttpClientTool.getAbsoluteUrl(jsonUser.getU_small_avatar()), headImageView,
					ImageLoaderTool.getHeadImageOptions(10));
			//点击显示高清头像
			headImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(PersonDetailActivity.this, ImageShowerActivity.class);
					intent.putExtra(ImageShowerActivity.SHOW_BIG_IMAGE,
							AsyncHttpClientTool.getAbsoluteUrl(jsonUser.getU_large_avatar()));
					startActivity(intent);
					PersonDetailActivity.this.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				}
			});
		}
		//设置姓名、省份、及学校

		basicInfo.setText(jsonUser.getU_gender() + " | "
				+ SchoolDbService.getInstance(getApplicationContext()).getSchoolNameById(jsonUser.getU_schoolid())
				+ " | " + jsonUser.getU_identity() + " | " + jsonUser.getU_love_tate());
		introduce.setText(jsonUser.getU_introduce());
	}

	/**
	 * 	网络获取User信息
	 */
	private void getUser() {
		final ProgressDialog dialog = new ProgressDialog(PersonDetailActivity.this);
		dialog.setMessage("正在加载...");
		dialog.setCancelable(false);
		RequestParams params = new RequestParams();
		params.put(UserTable.U_ID, userId);

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
					jsonUser = FastJsonTool.getObject(response, JsonUser.class);
					if (jsonUser != null) {
						initPersonView();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("获取用户服务器错误");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				dialog.dismiss();
			}
		};
		AsyncHttpClientTool.post(PersonDetailActivity.this, "user/getInfoByID", params, responseHandler);
	}

	/**
	 * 是否关注过此人
	 */
	private void getConcerned(int userID) {
		RequestParams params = new RequestParams();
		params.put(QuanziTable.C_BECONCERNED_USERID, userID);
		params.put(QuanziTable.C_USERID, userPreference.getU_id());

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					if (response.equals("1")) {
						isConcerned = true;
						concernBtn.setVisibility(View.VISIBLE);
						concernBtn.setChecked(true);
					} else if (response.equals("0")) {
						isConcerned = false;
						concernBtn.setVisibility(View.VISIBLE);
						concernBtn.setChecked(false);
					} else {
						LogTool.e("个人主页", "获取关注状态返回错误" + response);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("个人主页", "获取关注状态失败");
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

		};
		AsyncHttpClientTool.post(PersonDetailActivity.this, "quanzi/isConcerned", params, responseHandler);
	}

	/**
	 * 关注
	 */
	private void concern(int userID) {

		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put(QuanziTable.C_BECONCERNED_USERID, userID);
		params.put(QuanziTable.C_USERID, userPreference.getU_id());

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler("utf-8") {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					if (response.equals("1")) {
						LogTool.i("关注成功！");
						isConcerned = !isConcerned;
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
		if (!isConcerned) {
			AsyncHttpClientTool.post(PersonDetailActivity.this, "quanzi/concern", params, responseHandler);
		} else {
			AsyncHttpClientTool.post(PersonDetailActivity.this, "quanzi/undoConcern", params, responseHandler);
		}
	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.postBtn:
			index = 0;
			break;
		case R.id.dataBtn:
			index = 1;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.nav_right_btn2://私信
			Intent toChatIntent = new Intent(PersonDetailActivity.this, ChatActivity.class);
			toChatIntent.putExtra("userId", "" + jsonUser.getU_id());
			toChatIntent.putExtra("jsonuser", jsonUser);
			startActivity(toChatIntent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.nav_right_btn1://更多
			showMoreDialog();
			break;
		case R.id.postBtn:
			onTabClicked(v);
			break;
		case R.id.dataBtn:
			onTabClicked(v);
			break;
		case R.id.concern_btn:
			concern(userId);
			break;
		default:
			break;
		}
	}

}
