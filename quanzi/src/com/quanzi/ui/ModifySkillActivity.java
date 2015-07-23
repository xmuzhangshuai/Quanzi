package com.quanzi.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
 * 类名称：ModifySkillActivity  
 * 类描述：修改技能
 * @author zhangshuai
 * @date 创建时间：2015-4-30 下午6:46:26 
 *
 */
public class ModifySkillActivity extends BaseActivity implements OnClickListener {

	/***********VIEWS************/
	private ListView industryListView;
	private TextView saveBtn;
	private View backBtn;
	TextView[] skillTextViews = new TextView[3];
	private EditText ownDefinedEditText;
	private TextView addBtn;
	private UserPreference userPreference;
	private List<String> skillList;
	private List<String> mySkillList;
	private String skills;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_skill);
		userPreference = BaseApplication.getInstance().getUserPreference();
		skills = userPreference.getU_skills();
		skillList = new ArrayList<String>();
		mySkillList = new ArrayList<String>();

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
		skillTextViews[0] = (TextView) findViewById(R.id.skill1);
		skillTextViews[1] = (TextView) findViewById(R.id.skill2);
		skillTextViews[2] = (TextView) findViewById(R.id.skill3);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		saveBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);

		if (!skills.isEmpty()) {
			String[] skillStrings = skills.split("\\|");
			for (int i = 0; i < skillStrings.length; i++) {
				mySkillList.add(skillStrings[i]);
			}
		}

		refresh();

		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mySkillList.size() < 3) {
					String newSkillString = ownDefinedEditText.getText().toString().trim();
					if (newSkillString != null && !newSkillString.isEmpty() && !skillList.contains(newSkillString)) {
						mySkillList.add(ownDefinedEditText.getText().toString().trim());
						ownDefinedEditText.setText("");
						refresh();
					}
				}
			}
		});

		skillTextViews[0].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteSkill(skillTextViews[0].getText().toString().trim());
			}
		});
		skillTextViews[1].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteSkill(skillTextViews[1].getText().toString().trim());
			}
		});
		skillTextViews[2].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteSkill(skillTextViews[2].getText().toString().trim());
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
			for (String s : mySkillList) {
				skillString = skillString + "|" + s;
			}
			skillString = skillString.substring(1, skillString.length());
			updateSkill(skillString);
			break;
		default:
			break;
		}
	}

	private void refresh() {
		for (int j = 0; j < skillTextViews.length; j++) {
			skillTextViews[j].setVisibility(View.GONE);
		}
		for (int j = 0; j < mySkillList.size(); j++) {
			skillTextViews[j].setText(mySkillList.get(j));
			skillTextViews[j].setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 删除技能
	 */
	private void deleteSkill(final String skill) {
		final MyAlertDialog dialog = new MyAlertDialog(ModifySkillActivity.this);
		dialog.setTitle("删除");
		dialog.setMessage("确定要删除" + skill + "？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (mySkillList.contains(skill)) {
					mySkillList.remove(skill);
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
						skillList = FastJsonTool.getObjectList(response, String.class);
						if (skillList != null && skillList.size() > 0) {
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
		AsyncHttpClientTool.post(ModifySkillActivity.this, "quanzi/getSkillList", params, responseHandler);
	}

	/**
	* 修改技能
	* 
	* @param context
	* @param isCrop
	*/
	public void updateSkill(final String skills) {
		if (!skills.equals(userPreference.getU_skills())) {
			// 没有错误，则修改
			RequestParams params = new RequestParams();
			params.put(UserTable.U_ID, userPreference.getU_id());
			params.put(UserTable.U_SKILL_ITEMS, skills.trim());

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
							ToastTool.showShort(ModifySkillActivity.this, "修改成功！");
							userPreference.setU_skills(skills);
							finish();
						} else if (response.equals("-1")) {
							LogTool.e("修改技能返回-1");
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					// TODO Auto-generated method stub
					LogTool.e("修改技能服务器错误");
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
			return skillList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return skillList.get(position);
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
			final String name = skillList.get(position);
			if (name == null) {
				return null;
			}

			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(ModifySkillActivity.this).inflate(R.layout.simple_list_item, null);
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
					if (mySkillList.size() < 3) {
						if (name != null && !name.isEmpty() && !mySkillList.contains(name)) {
							mySkillList.add(name);
							refresh();
						}
					}
				}
			});
			return view;
		}
	}
}
