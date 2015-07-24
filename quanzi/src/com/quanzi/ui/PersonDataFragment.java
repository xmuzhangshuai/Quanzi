package com.quanzi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.db.SchoolDbService;
import com.quanzi.jsonobject.JsonUser;
import com.quanzi.utils.DateTimeTools;
import com.quanzi.utils.UserPreference;

/**
 *
 * ��Ŀ���ƣ�quanzi  
 * �����ƣ�PersonDataFragment  
 * �����������˸������ĵ�������ҳ��
 * @author zhangshuai
 * @date ����ʱ�䣺2015-5-11 ����10:55:28 
 *
 */
public class PersonDataFragment extends BaseV4Fragment {
	private View rootView;// ��View

	private JsonUser jsonUser;

	/***�ǳ�***/
	private View nicknameView;
	private TextView nickNameTextView;

	/***�Ա�***/
	private View genderView;
	private TextView genderText;

	/***����***/
	private View birthdayView;
	private TextView birthdayTextView;

	/***ѧУ***/
	private View schoolView;
	private TextView schoolTextView;

	/***��ǰ���***/
	private View statusView;
	private TextView statusTextView;

	/***���״̬***/
	private View loveStatusView;
	private TextView loveStatusTextView;

	/***��Ȥ����***/
	private View interestView;
	private TextView interestTextView;

	/***�ó�����***/
	private View skillView;
	private TextView skillTextView;

	/***������ҵ***/
	private View industryView;
	private TextView industryTextView;

	/***����ǩ��***/
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

		findViewById();// ��ʼ��views
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

			/***�ǳ�***/
			if (jsonUser.getU_nickname() == null || jsonUser.getU_nickname().isEmpty()) {
				nickNameTextView.setText("δ��д");
			} else {
				nickNameTextView.setText(jsonUser.getU_nickname());
			}

			/***�Ա�***/
			genderText.setText(jsonUser.getU_gender());

			/***����***/
			birthdayTextView.setText(DateTimeTools.DateToString(jsonUser.getU_birthday()));

			/***ѧУ***/
			schoolTextView.setText(SchoolDbService.getInstance(getActivity()).getSchoolNameById(
					jsonUser.getU_schoolid()));

			/***��ǰ���***/
			statusTextView.setText(jsonUser.getU_identity());

			/***���״̬***/
			loveStatusTextView.setText(jsonUser.getU_love_state());

			/***��Ȥ����***/
			interestTextView.setText(jsonUser.getU_interest_items().replace("|", "\n"));

			/***�ó�����***/
			skillTextView.setText(jsonUser.getU_skill_items().replace("|", " | "));

			/***������ҵ***/
			industryTextView.setText(jsonUser.getU_industry_item());

			/***����ǩ��***/
			if (jsonUser.getU_introduce() == null || jsonUser.getU_introduce().isEmpty()) {
				introTextView.setText("δ��д");
			} else {
				introTextView.setText(jsonUser.getU_introduce());
			}
		}
	}
}
