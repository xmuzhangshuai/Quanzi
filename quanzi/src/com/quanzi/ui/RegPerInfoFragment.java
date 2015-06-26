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
 * ��Ŀ���ƣ�quanquan  
 * �����ƣ�PersonalInfoFragment  
 * ��������ѡ���ǳƺ��Ա�ҳ��
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-6 ����8:30:13 
 *
 */
public class RegPerInfoFragment extends BaseV4Fragment implements OnCheckedChangeListener {
	/*************Views************/
	private View rootView;// ��View

	private RadioButton mMale;//��
	private RadioButton mFemale;//Ů
	private EditText mNameView;//�ǳ�
	private TextView topNavigation;//����������
	private TextView leftNavigation;//����
	private View leftImageButton;//��������ఴť
	private View rightImageButton;//�������Ҳఴť

	private UserPreference userPreference;
	private String mName;
	private boolean flag = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_reg_perinfo, container, false);
		userPreference = BaseApplication.getInstance().getUserPreference();

		findViewById();// ��ʼ��views
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
		topNavigation.setText("������Ϣ");
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
	 * ȷ����ֹע��
	 */
	private void vertifyToTerminate() {
		final MyAlertDialog dialog = new MyAlertDialog(getActivity());
		dialog.setTitle("��ʾ");
		dialog.setMessage("ע��������˳�����Ϣ�����ܱ��档�Ƿ�����˳���");
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
		dialog.setPositiveButton("ȷ��", comfirm);
		dialog.setNegativeButton("ȡ��", cancle);
		dialog.show();
	}

	/**
	 * ȷ���Ա𲻿��޸�
	 */
	private void vertifyGender() {
		if (flag) {
			next();
			return;
		} else {
			final MyAlertDialog dialog = new MyAlertDialog(getActivity());
			dialog.setShowCancel(false);
			dialog.setTitle("��ʾ");
			dialog.setMessage("�Ա�ȷ���󣬽����ɸ���");
			View.OnClickListener comfirm = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					next();
				}
			};
			dialog.setPositiveButton("ȷ��", comfirm);
			dialog.show();
			flag = true;
		}

	}

	/**
	 * ����Ƿ���ѡ
	 */
	private void attemptNext() {
		mNameView.setError(null);

		// �洢�û�ֵ
		mName = mNameView.getText().toString().trim();
		boolean cancel = false;
		View focusView = null;

		//����ǳ�
		if (TextUtils.isEmpty(mName)) {
			mNameView.setError(getString(R.string.error_field_required));
			focusView = mNameView;
			cancel = true;
		}

		//����Ƿ�ѡ�Ա�
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
			// �����������ʾ����
			focusView.requestFocus();
			rightImageButton.setEnabled(false);
		} else {
			// û�д�����洢ֵ
			rightImageButton.setEnabled(true);
			userPreference.setU_nickname(mName);
		}
	}

	/**
	 * ��һ��
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
