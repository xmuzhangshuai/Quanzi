package com.quanzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.db.SchoolDbService;
import com.quanzi.jsonobject.JsonUser;
import com.quanzi.table.IndustryTable;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanzi 类名称：PersonDataFragment 类描述：他人个人中心的资料子页面
 * 
 * @author zhangshuai
 * @date 创建时间：2015-5-11 上午10:55:28
 *
 */
public class PersonDataFragment extends BaseV4Fragment {
	private View rootView;// 根View

	private JsonUser jsonUser;

	/*** 昵称 ***/
	private View nicknameView;
	private TextView nickNameTextView;

	/*** 性别 ***/
	private View genderView;
	private TextView genderText;

	/*** 生日 ***/
	private View birthdayView;
	private TextView birthdayTextView;

	/*** 学校 ***/
	private View schoolView;
	private TextView schoolTextView;

	/*** 当前身份 ***/
	private View statusView;
	private TextView statusTextView;

	/*** 情感状态 ***/
	private View loveStatusView;
	private TextView loveStatusTextView;

	/*** 兴趣爱好 ***/
	private View interestView;
	private TextView interestTextView;

	/*** 擅长技能 ***/
	private View skillView;
	private TextView skillTextView;

	/*** 所属行业 ***/
	private View industryView;
	private TextView industryTextView;

	/*** 个人签名 ***/
	private View introView;
	private TextView introTextView;

	private UserPreference userPreference;

	public PersonDataFragment(JsonUser jsonUser) {
		super();
		// TODO Auto-generated constructor stub
		this.jsonUser = jsonUser;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_person_data, container, false);
		userPreference = BaseApplication.getInstance().getUserPreference();

		findViewById();// 初始化views
		initView();

		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		nicknameView = rootView.findViewById(R.id.nicknameview);
		nickNameTextView = (TextView) rootView.findViewById(R.id.nickname);
		genderView = rootView.findViewById(R.id.genderview);
		genderText = (TextView) rootView.findViewById(R.id.gender);
		birthdayView = rootView.findViewById(R.id.birthdayview);
		birthdayTextView = (TextView) rootView.findViewById(R.id.birthday);
		schoolView = rootView.findViewById(R.id.schoolview);
		schoolTextView = (TextView) rootView.findViewById(R.id.school);
		statusView = rootView.findViewById(R.id.statusView);
		statusTextView = (TextView) rootView.findViewById(R.id.status);
		loveStatusView = rootView.findViewById(R.id.loveStatusView);
		loveStatusTextView = (TextView) rootView.findViewById(R.id.lovestatus);
		interestView = rootView.findViewById(R.id.interestView);
		interestTextView = (TextView) rootView.findViewById(R.id.interest);
		skillView = rootView.findViewById(R.id.skillView);
		skillTextView = (TextView) rootView.findViewById(R.id.skill);
		industryView = rootView.findViewById(R.id.industryView);
		industryTextView = (TextView) rootView.findViewById(R.id.industry);
		introView = rootView.findViewById(R.id.personIntroview);
		introTextView = (TextView) rootView.findViewById(R.id.personIntro);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		if (jsonUser != null) {

			/*** 昵称 ***/
			if (jsonUser.getU_nickname() == null || jsonUser.getU_nickname().isEmpty()) {
				nickNameTextView.setText("未填写");
			} else {
				nickNameTextView.setText(jsonUser.getU_nickname());
			}

			/*** 性别 ***/
			genderText.setText(jsonUser.getU_gender());

			/*** 生日 ***/
			birthdayTextView.setText(DateTimeTools.DateToString(jsonUser.getU_birthday()));

			/*** 学校 ***/
			schoolTextView
					.setText(SchoolDbService.getInstance(getActivity()).getSchoolNameById(jsonUser.getU_schoolid()));

			/*** 当前身份 ***/
			statusTextView.setText(jsonUser.getU_identity());

			/*** 情感状态 ***/
			loveStatusTextView.setText(jsonUser.getU_love_state());

			/*** 兴趣爱好 ***/
			if (!TextUtils.isEmpty(jsonUser.getU_interest_items())) {
				interestTextView.setText(jsonUser.getU_interest_items().replace("|", "\n"));
			}
			
			/*** 擅长技能 ***/
			if (!TextUtils.isEmpty(jsonUser.getU_skill_items())) {
				skillTextView.setText(jsonUser.getU_skill_items().replace("|", "\n"));
			}

			/*** 所属行业 ***/
			if (jsonUser.getU_industry_item() != null && jsonUser.getU_industry_item().length() > 0) {
				industryTextView.setText(jsonUser.getU_industry_item());
				industryTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(), MyPeopleActivity.class);
						intent.putExtra(MyPeopleActivity.TYPE, MyPeopleActivity.SAME_IN_INDUSTRY);
						intent.putExtra(IndustryTable.I_NAME, jsonUser.getU_industry_item());
						startActivity(intent);
						getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}
				});
			}

			/*** 个人签名 ***/
			if (jsonUser.getU_introduce() == null || jsonUser.getU_introduce().isEmpty()) {
				introTextView.setText("未填写");
			} else {
				introTextView.setText(jsonUser.getU_introduce());
			}
		}
	}
}
