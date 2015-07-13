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
 * 项目名称：quanzi  
 * 类名称：PersonDataFragment  
 * 类描述：他人个人中心的资料子页面
 * @author zhangshuai
 * @date 创建时间：2015-5-11 上午10:55:28 
 *
 */
public class PersonDataFragment extends BaseV4Fragment {
	private View rootView;// 根View

	/***昵称***/
	private View nicknameView;
	private TextView nickNameTextView;

	/***性别***/
	private View genderView;
	private TextView genderText;

	/***生日***/
	private View birthdayView;
	private TextView birthdayTextView;

	/***学校***/
	private View schoolView;
	private TextView schoolTextView;

	/***当前身份***/
	private View statusView;
	private TextView statusTextView;

	/***情感状态***/
	private View loveStatusView;
	private TextView loveStatusTextView;

	/***兴趣爱好***/
	private View interestView;
	private TextView interestTextView;

	/***擅长技能***/
	private View skillView;
	private TextView skillTextView;

	/***所属行业***/
	private View industryView;
	private TextView industryTextView;

	/***个人签名***/
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
		/***昵称***/
		if (userPreference.getU_nickname().isEmpty()) {
			nickNameTextView.setText("未填写");
		}else {
			nickNameTextView.setText(userPreference.getU_nickname());
		}

		/***性别***/
		genderText.setText(userPreference.getU_gender());

		/***生日***/
		birthdayTextView.setText(DateTimeTools.DateToString(userPreference.getU_birthday()));

		/***学校***/
		 schoolTextView.setText(userPreference.getSchoolName());

		/***当前身份***/
		statusTextView.setText(userPreference.getU_identity());

//		/***情感状态***/
//		loveStatusTextView;
//
//		/***兴趣爱好***/
//		interestTextView;
//
//		/***擅长技能***/
//		skillTextView;
//
//		/***所属行业***/
//		industryTextView.setText();

		/***个人签名***/
		if (userPreference.getU_introduce().isEmpty()) {
			introTextView.setText("未填写");
		}else {
			introTextView.setText(userPreference.getU_introduce());
		}
	}
}
