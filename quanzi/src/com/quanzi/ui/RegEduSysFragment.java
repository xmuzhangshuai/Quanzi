package com.quanzi.ui;

import org.apache.http.Header;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseApplication;
import com.quanzi.base.BaseV4Fragment;
import com.quanzi.jsonobject.JsonUser;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.CommonTools;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 *
 * ��Ŀ���ƣ�quanquan  
 * �����ƣ�RegEduSysFragment  
 * ��������ͨ����������ķ�ʽ��֤����ϵͳ����
 * @author zhangshuai
 * @date ����ʱ�䣺2015-4-8 ����9:48:06 
 *
 */
public class RegEduSysFragment extends BaseV4Fragment {
	private View rootView;// ��View
	// UI references.
	private EditText mStudentNumberView;//ѧ��
	private EditText mPasswordView;//����
	private View mProgressView;//����
	private TextView topNavigation;//����������
	private TextView leftNavigation;//����
	private View leftImageButton;//��������ఴť
	private View rightImageButton;//�������Ҳఴť
	private UserPreference userPreference;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_reg_edusys, container, false);
		userPreference = BaseApplication.getInstance().getUserPreference();
		findViewById();// ��ʼ��views
		initView();
		return rootView;
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mStudentNumberView = (EditText) rootView.findViewById(R.id.student_number);
		mPasswordView = (EditText) rootView.findViewById(R.id.password);
		mProgressView = rootView.findViewById(R.id.login_status);
		topNavigation = (TextView) getActivity().findViewById(R.id.nav_text);
		leftImageButton = (View) getActivity().findViewById(R.id.left_btn_bg);
		rightImageButton = (View) getActivity().findViewById(R.id.right_btn_bg);
		leftNavigation = (TextView) getActivity().findViewById(R.id.left_text);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		topNavigation.setText("����ϵͳ�����֤");
		leftNavigation.setText("4/4");
		leftImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});

		rightImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				attemptEduSys();
			}
		});

	}

	/**
	 * ��֤����ϵͳ
	 */
	private void attemptEduSys() {
		// Reset errors.
		mStudentNumberView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String studentNumber = mStudentNumberView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid email address.
		if (TextUtils.isEmpty(studentNumber)) {
			mStudentNumberView.setError(getString(R.string.error_field_required));
			focusView = mStudentNumberView;
			cancel = true;
		} else if (!CommonTools.isStudentNO(studentNumber)) {
			mStudentNumberView.setError(getString(R.string.error_invalid_studentnum));
			focusView = mStudentNumberView;
			cancel = true;
		}

		// Check for a valid password, if the user entered one.
		else if (TextUtils.isEmpty(password)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (!CommonTools.isPassValid(password)) {
			mPasswordView.setError(getString(R.string.error_pattern_password));
			focusView = mPasswordView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);

			// û�д�����洢ֵ
			userPreference.setU_student_number(studentNumber);
			userPreference.setU_student_pass(password);

			register();

		}
	}

	//ע���������
	private void register() {
		// û�д������޸�
		RequestParams params = new RequestParams();
		JsonUser jsonUser = new JsonUser(userPreference.getU_nickname(), userPreference.getU_password(),
				userPreference.getU_gender(), userPreference.getU_tel(), userPreference.getU_provinceid(),
				userPreference.getU_cityid(), userPreference.getU_schoolid(), userPreference.getU_student_number(),
				userPreference.getU_student_pass());

		params.put("register", FastJsonTool.createJsonString(jsonUser));

		TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showProgress(true);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				showProgress(false);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					if (!response.isEmpty()) {
						int userID = Integer.parseInt(response.trim());
						if (userID > 0) {
							userPreference.setU_id(userID);
							userPreference.setUserLogin(true);
							loginHuanxin("" + userID, userPreference.getU_password());
						} else {
							LogTool.e("ע��ʱ���ظ���" + response);
						}
					} else {
						LogTool.e("ע��ʱ����Ϊ��");
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				ToastTool.showLong(getActivity(), "ע��ʧ��");
				LogTool.e("ע��ʱ����������");
				showProgress(false);
			}
		};
		AsyncHttpClientTool.post("user/regist", params, responseHandler);
	}

	/**
	 * ��¼����
	 */
	private void loginHuanxin(String userName, String password) {
		EMChatManager.getInstance().login(userName, password, new EMCallBack() {//�ص�
					@Override
					public void onSuccess() {
						EMChatManager.getInstance().loadAllConversations();

						LogTool.i("����", "��½����������ɹ���");

						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								Intent intent = new Intent(getActivity(), RegSuccessActivity.class);
								startActivity(intent);
								getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						LogTool.e("����", "��½���������ʧ�ܣ�" + message);
					}
				});
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (show) {
			//���������   
			((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}

}
