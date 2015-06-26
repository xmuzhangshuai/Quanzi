package com.quanzi.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.config.Constants;
import com.quanzi.customewidget.MyAlertDialog;
import com.quanzi.utils.UserPreference;

/**
 *
 * 项目名称：quanquan  
 * 类名称：PersonalInfoFragment  
 * 类描述：选择昵称和性别页面
 * @author zhangshuai
 * @date 创建时间：2015-4-6 下午8:30:13 
 *
 */
public class RegPerInfoFragment extends BaseV4Fragment implements OnCheckedChangeListener {
	/*************Views************/
	private View rootView;// 根View

	private RadioButton mMale;//男
	private RadioButton mFemale;//女
	private EditText mNameView;//昵称
	private TextView topNavigation;//导航栏文字
	private TextView leftNavigation;//步骤
	private View leftImageButton;//导航栏左侧按钮
	private View rightImageButton;//导航栏右侧按钮

	private UserPreference userPreference;
	private String mName;
	private boolean flag = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_reg_perinfo, container, false);
		userPreference = BaseApplication.getInstance().getUserPreference();

		findViewById();// 初始化views
		initView();
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mMale = (RadioButton) rootView.findViewById(R.id.male);
		mFemale = (RadioButton) rootView.findViewById(R.id.female);
		mNameView = (EditText) rootView.findViewById(R.id.name);
		topNavigation = (TextView) getActivity().findViewById(R.id.nav_text);
		leftImageButton = (View) getActivity().findViewById(R.id.left_btn_bg);
		rightImageButton = (View) getActivity().findViewById(R.id.right_btn_bg);
		leftNavigation = (TextView) getActivity().findViewById(R.id.left_text);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		topNavigation.setText("个人信息");
		leftNavigation.setText("1/4");
		rightImageButton.setEnabled(false);
		rightImageButton.setVisibility(View.VISIBLE);
		leftImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vertifyToTerminate();
			}
		});
		rightImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vertifyGender();
			}
		});
		mMale.setOnCheckedChangeListener(this);
		mFemale.setOnCheckedChangeListener(this);
		mNameView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				attemptNext();
			}
		});
	}

	/**
	 * 确认终止注册
	 */
	private void vertifyToTerminate() {
		final MyAlertDialog dialog = new MyAlertDialog(getActivity());
		dialog.setTitle("提示");
		dialog.setMessage("注册过程中退出，信息将不能保存。是否继续退出？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				getActivity().finish();
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
	 * 确认性别不可修改
	 */
	private void vertifyGender() {
		if (flag) {
			next();
			return;
		} else {
			final MyAlertDialog dialog = new MyAlertDialog(getActivity());
			dialog.setShowCancel(false);
			dialog.setTitle("提示");
			dialog.setMessage("性别确定后，将不可更改");
			View.OnClickListener comfirm = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					next();
				}
			};
			dialog.setPositiveButton("确定", comfirm);
			dialog.show();
			flag = true;
		}

	}

	/**
	 * 检查是否都已选
	 */
	private void attemptNext() {
		mNameView.setError(null);

		// 存储用户值
		mName = mNameView.getText().toString().trim();
		boolean cancel = false;
		View focusView = null;

		//检查昵称
		if (TextUtils.isEmpty(mName)) {
			mNameView.setError(getString(R.string.error_field_required));
			focusView = mNameView;
			cancel = true;
		}

		//检查是否选性别
		else if (!mMale.isChecked() && !mFemale.isChecked()) {
			cancel = true;
			focusView = mFemale;
			rightImageButton.setEnabled(false);
		} else if (mMale.isChecked()) {
			userPreference.setU_gender(Constants.Gender.MALE);
		} else if (mFemale.isChecked()) {
			userPreference.setU_gender(Constants.Gender.FEMALE);
		}

		if (cancel) {
			// 如果错误，则提示错误
			focusView.requestFocus();
			rightImageButton.setEnabled(false);
		} else {
			// 没有错误则存储值
			rightImageButton.setEnabled(true);
			userPreference.setU_nickname(mName);
		}
	}

	/**
	 * 下一步
	 */
	private void next() {
		RegAccountFragment regAccountFragment = new RegAccountFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in,
				R.anim.push_right_out);
		transaction.replace(R.id.fragment_container, regAccountFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		attemptNext();
	}
}
