package com.quanzi.ui;

import java.util.List;

import org.apache.http.Header;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.quanzi.R;
import com.quanzi.base.BaseActivity;
import com.quanzi.base.BaseApplication;
import com.quanzi.jsonobject.JsonUser;
import com.quanzi.table.UserTable;
import com.quanzi.utils.AsyncHttpClientTool;
import com.quanzi.utils.CommonTools;
import com.quanzi.utils.FastJsonTool;
import com.quanzi.utils.LogTool;
import com.quanzi.utils.MD5For32;
import com.quanzi.utils.SIMCardInfo;
import com.quanzi.utils.ToastTool;
import com.quanzi.utils.UserPreference;

/**
 * �����ƣ�LoginActivity 
 * ����������¼ҳ�� 
 * �����ˣ� ��˧ 
 * ����ʱ�䣺2014-7-4 ����9:34:33
 * 
 */
public class LoginActivity extends BaseActivity {

	// UI references.
	private EditText mPhoneView;//�ֻ���
	private EditText mPasswordView;//����
	private View mProgressView;//����
	private TextView topNavigation;//����������
	private View leftImageButton;//��������ఴť
	private View rightImageButton;//�������Ҳఴť
	private UserPreference userPreference;
	private TextView forgetPassword;//��������
	private Button loginButton;//��¼

	List<JsonUser> jsonUsers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		userPreference = BaseApplication.getInstance().getUserPreference();
		findViewById();
		initView();

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mPhoneView = (EditText) findViewById(R.id.phone);
		mPasswordView = (EditText) findViewById(R.id.password);
		mProgressView = findViewById(R.id.login_status);
		topNavigation = (TextView) findViewById(R.id.nav_text);
		leftImageButton = (View) findViewById(R.id.left_btn_bg);
		rightImageButton = (View) findViewById(R.id.right_btn_bg);
		forgetPassword = (TextView) findViewById(R.id.forget_password);
		loginButton = (Button) findViewById(R.id.login);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		String loginedPhone = userPreference.getU_tel();
		if (!TextUtils.isEmpty(loginedPhone)) {
			mPhoneView.setText(loginedPhone);
		} else {
			SIMCardInfo siminfo = new SIMCardInfo(LoginActivity.this);
			String number = siminfo.getNativePhoneNumber();
			mPhoneView.setText(number);
		}

		topNavigation.setText("��¼");
		leftImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				attemptLogin();
			}
		});
		rightImageButton.setVisibility(View.INVISIBLE);

		forgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {

		// Reset errors.
		mPhoneView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String phone = mPhoneView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (TextUtils.isEmpty(password)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (!CommonTools.isPassValid(password)) {
			mPasswordView.setError(getString(R.string.error_pattern_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		else if (TextUtils.isEmpty(phone)) {
			mPhoneView.setError(getString(R.string.error_field_required));
			focusView = mPhoneView;
			cancel = true;
		} else if (!CommonTools.isMobileNO(phone)) {
			mPhoneView.setError(getString(R.string.error_invalid_phone));
			focusView = mPhoneView;
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
			login(phone, MD5For32.GetMD5Code(password));
		}

	}

	//��¼
	private void login(String tel, final String pass) {

		RequestParams params = new RequestParams();
		params.put(UserTable.U_TEL, tel);
		params.put(UserTable.U_PASSWORD, pass);

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
				showProgress(false);
				super.onFinish();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String response) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					LogTool.i("���ص���Ϣ:", response);
					if (!response.isEmpty()) {
						if (response.equals("-1")) {
							mPasswordView.setError("�绰���������");
							mPasswordView.requestFocus();
							showProgress(false);
						} else {
							JsonUser user = FastJsonTool.getObject(response, JsonUser.class);
							if (user != null) {
								saveUser(user);//�����û���Ϣ
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								startActivity(intent);
								overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
							} else {
								LogTool.e("��¼���س���,userΪ��");
							}
						}
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				LogTool.e("����������" + errorResponse);
			}
		};
		AsyncHttpClientTool.post("user/login", params, responseHandler);
	}

	/**
	 * �洢�Լ�����Ϣ
	 */
	private void saveUser(final JsonUser user) {
		// TODO Auto-generated method stub
		LogTool.i("ServerUtil", "�洢������Ϣ");
		userPreference.setU_id(user.getU_id());
		userPreference.setU_nickname(user.getU_nickname());
		userPreference.setU_password(user.getU_password());
		userPreference.setU_gender(user.getU_gender());
		userPreference.setU_tel(user.getU_tel());
		userPreference.setU_email(user.getU_email());
		userPreference.setU_birthday(user.getU_birthday());
		userPreference.setU_age(user.getU_age());
		userPreference.setU_large_avatar(user.getU_large_avatar());
		userPreference.setU_small_avatar(user.getU_small_avatar());
		userPreference.setU_identity(user.getU_identity());
		userPreference.setU_love_state(user.getU_love_state());
		userPreference.setU_provinceid(user.getU_provinceid());
		userPreference.setU_cityid(user.getU_cityid());
		userPreference.setU_schoolid(user.getU_schoolid());
		userPreference.setU_interest_ids(user.getU_interest_ids());
		userPreference.setU_skill_ids(user.getU_skill_ids());
		userPreference.setU_industry_id(user.getU_industry_id());
		userPreference.setU_introduce(user.getU_introduce());
		userPreference.setU_student_number(user.getU_student_number());
		userPreference.setU_student_pass(user.getU_stundet_pass());
		userPreference.setUserLogin(true);
	}

	/**
	 * ��¼����
	 */
	//	private void loginHuanXin() {
	//		EMChatManager.getInstance().login(userPreference.getHuanXinUserName(), userPreference.getHuanXinPassword(),
	//				new EMCallBack() {
	//
	//					@Override
	//					public void onSuccess() {
	//						userPreference.setUserLogin(true);
	//						//���»����ǳ�
	//						if (EMChatManager.getInstance().updateCurrentUserNick(userPreference.getName())) {
	//							LogTool.i("LoginActivity", "���»����ǳƳɹ�");
	//						} else {
	//							LogTool.e("LoginActivity", "���»����ǳ�ʧ��");
	//						}
	//						runOnUiThread(new Runnable() {
	//							public void run() {
	//								ServerUtil.getInstance().getTodayRecommend(LoginActivity.this, true);
	//								showProgress(false);
	//							}
	//						});
	//					}
	//
	//					@Override
	//					public void onProgress(int progress, String status) {
	//					}
	//
	//					@Override
	//					public void onError(int code, final String message) {
	//						LogTool.e("��¼����", "code:" + code + "   message:" + message);
	//						userPreference.clear();
	//						runOnUiThread(new Runnable() {
	//							public void run() {
	//								ToastTool.showShort(LoginActivity.this, "��¼���������ʧ��");
	//								showProgress(false);
	//							}
	//						});
	//					}
	//				});
	//
	//	}

	/**
	 * ��½
	 * 
	 * @param view
	 */
	//	public void attempLoginHuanXin(int time) {
	//		if (!NetworkUtils.isNetworkAvailable(this)) {
	//			NetworkUtils.networkStateTips(this);
	//			return;
	//		}
	//
	//		// ����sdk��½������½���������
	//		if (!TextUtils.isEmpty(userPreference.getHuanXinUserName())
	//				&& !TextUtils.isEmpty(userPreference.getHuanXinPassword())) {
	//			loginHuanXin();
	//		} else {
	//			if (time == 1) {
	//				userPreference.setHuanXinUserName("" + userPreference.getU_id());
	//				userPreference.setHuanXinPassword(MD5For16.GetMD5CodeToLower(userPreference.getU_password()));
	//				attempLoginHuanXin(2);
	//			} else {
	//				showProgress(false);
	//			}
	//		}
	//	}
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
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(LoginActivity.this
					.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
