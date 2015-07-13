package com.quanzi.ui;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.quanzi.R;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.ui.MainExplorePostFragment.PostAdapter;
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

	private String personIntro;
	private UserPreference userPreference;
	private File picFile;
	private Uri photoUri;
	private InputMethodManager imm;
	private String nickname;
	private final int activity_result_camara_with_data = 1006;
	private final int activity_result_cropimage_with_data = 1007;
	boolean nickNameChanged = false;
	boolean ageChanged = false;
	boolean weightChanged = false;
	boolean heightChanged = false;
	boolean constellChanged = false;
	boolean personIntroChanged = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_person_data, container, false);

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
		/***�ǳ�***/
		if (userPreference.getU_nickname().isEmpty()) {
			nickNameTextView.setText("δ��д");
		}else {
			nickNameTextView.setText(userPreference.getU_nickname());
		}

		/***�Ա�***/
		genderText.setText(userPreference.getU_gender());

		/***����***/
		birthdayTextView.setText(DateTimeTools.DateToString(userPreference.getU_birthday()));

		/***ѧУ***/
		 schoolTextView.setText(userPreference.getSchoolName());

		/***��ǰ���***/
		statusTextView.setText(userPreference.getU_identity());

//		/***���״̬***/
//		loveStatusTextView;
//
//		/***��Ȥ����***/
//		interestTextView;
//
//		/***�ó�����***/
//		skillTextView;
//
//		/***������ҵ***/
//		industryTextView.setText();

		/***����ǩ��***/
		if (userPreference.getU_introduce().isEmpty()) {
			introTextView.setText("δ��д");
		}else {
			introTextView.setText(userPreference.getU_introduce());
		}
	}
}
