package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi  
 * 类名称：ModifyInterestActivity  
 * 类描述：修改兴趣爱好
 * @author zhangshuai
 * @date 创建时间：2015-4-30 下午6:46:51 
 *
 */
public class ModifyInterestActivity extends BaseActivity implements OnClickListener {

	/***********VIEWS************/
	private ListView industryListView;
	private TextView saveBtn;
	private View backBtn;
	TextView[] interestTextViews = new TextView[5];
	private EditText ownDefinedEditText;
	private TextView addBtn;
	private UserPreference userPreference;
	private List<String> interestList;
	private List<String> myInterestList;
	private String interests;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_interest);
		userPreference = BaseApplication.getInstance().getUserPreference();
		interests = userPreference.getU_interests();
		interestList = new ArrayList<String>();
		myInterestList = new ArrayList<String>();

		getSkillList();

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		saveBtn = (TextView) findViewById(R.id.publish_btn);
		backBtn = findViewById(R.id.left_btn_bg);
		industryListView = (ListView) findViewById(R.id.myquanzi_list);
		ownDefinedEditText = (EditText) findViewById(R.id.own_define_edit);
		addBtn = (TextView) findViewById(R.id.add_btn);
		interestTextViews[0] = (TextView) findViewById(R.id.skill1);
		interestTextViews[1] = (TextView) findViewById(R.id.skill2);
		interestTextViews[2] = (TextView) findViewById(R.id.skill3);
		interestTextViews[3] = (TextView) findViewById(R.id.skill4);
		interestTextViews[4] = (TextView) findViewById(R.id.skill5);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		saveBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);

		if (!interests.isEmpty()) {
			String[] skillStrings = interests.split("\\|");
			for (int i = 0; i < skillStrings.length; i++) {
				myInterestList.add(skillStrings[i]);
			}
		}

		refresh();

		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (myInterestList.size() < 5) {
					String newSkillString = ownDefinedEditText.getText().toString().trim();
					if (newSkillString != null && !newSkillString.isEmpty() && !interestList.contains(newSkillString)) {
						myInterestList.add(ownDefinedEditText.getText().toString().trim());
						ownDefinedEditText.setText("");
						refresh();
					}
				}
			}
		});

		interestTextViews[0].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteSkill(interestTextViews[0].getText().toString().trim());
			}
		});
		interestTextViews[1].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteSkill(interestTextViews[1].getText().toString().trim());
			}
		});
		interestTextViews[2].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteSkill(interestTextViews[2].getText().toString().trim());
			}
		});
		interestTextViews[3].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteSkill(interestTextViews[3].getText().toString().trim());
			}
		});
		interestTextViews[4].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteSkill(interestTextViews[4].getText().toString().trim());
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn_bg:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.publish_btn:
			String skillString = "";
			for (String s : myInterestList) {
				skillString = skillString + "|" + s;
			}
			if (skillString.length() > 0) {
				skillString = skillString.substring(1, skillString.length());
			}
			updateInterest(skillString);
			break;
		default:
			break;
		}
	}

	private void refresh() {
		for (int j = 0; j < interestTextViews.length; j++) {
			interestTextViews[j].setVisibility(View.GONE);
		}
		for (int j = 0; j < myInterestList.size(); j++) {
			interestTextViews[j].setText(myInterestList.get(j));
			interestTextViews[j].setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 删除技能
	 */
	private void deleteSkill(final String skill) {
		final MyAlertDialog dialog = new MyAlertDialog(ModifyInterestActivity.this);
		dialog.setTitle("删除");
		dialog.setMessage("确定要删除" + skill + "？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (myInterestList.contains(skill)) {
					myInterestList.remove(skill);
					refresh();
				}
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		};
		dialog.setPositiveButton("确定", comfirm);
		dialog.setNegativeButton("取消", cancle);
		dialog.show();
	}

	/**
	 * 	网络获取技能列表
	 */
	private void getSkillList() {
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
						interestList = FastJsonTool.getObjectList(response, String.class);
						if (interestList != null && interestList.size() > 0) {
							industryListView.setAdapter(new SkillAdapter());
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
		AsyncHttpClientTool.post(ModifyInterestActivity.this, "quanzi/getInterestList", params, responseHandler);
	}

	/**
	* 修改技能
	* 
	* @param context
	* @param isCrop
	*/
	public void updateInterest(final String interests) {
		if (!interests.equals(userPreference.getU_interests())) {
			// 没有错误，则修改
			RequestParams params = new RequestParams();
			params.put(UserTable.U_ID, userPreference.getU_id());
			params.put(UserTable.U_INTEREST_ITEMS, interests.trim());

			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				Dialog dialog;

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					dialog = showProgressDialog("请稍后...");
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					dialog.dismiss();
					super.onFinish();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String response) {
					// TODO Auto-generated method stub
					if (statusCode == 200) {
						if (response.equals("1")) {
							ToastTool.showShort(ModifyInterestActivity.this, "修改成功！");
							userPreference.setU_interests(interests);
							finish();
						} else if (response.equals("-1")) {
							LogTool.e("修改兴趣返回-1");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("修改兴趣服务器错误");
				}
			};
			AsyncHttpClientTool.post("user/infoUpdate", params, responseHandler);
		} else {

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
	private class SkillAdapter extends BaseAdapter {
		private class ViewHolder {
			public TextView nameTextView;
			public TextView countTextView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return interestList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return interestList.get(position);
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
			final String name = interestList.get(position);
			if (name == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(ModifyInterestActivity.this).inflate(R.layout.simple_list_item, null);
				holder = new ViewHolder();
				holder.nameTextView = (TextView) view.findViewById(R.id.name);
				holder.countTextView = (TextView) view.findViewById(R.id.count);
				view.setTag(holder); // 给View添加一个格外的数据 
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来  
			}

			holder.nameTextView.setText(name);
			holder.countTextView.setVisibility(View.GONE);

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (myInterestList.size() < 5) {
						if (name != null && !name.isEmpty() && !myInterestList.contains(name)) {
							myInterestList.add(name);
							refresh();
						}
					}
				}
			});
			return view;
		}
	}
}
